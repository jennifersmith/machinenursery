import cc.mallet.pipe.iterator.CsvIterator
import cc.mallet.topics.ParallelTopicModel
import cc.mallet.topics.TopicInferencer

import java.util.regex.Pattern

import cc.mallet.pipe.*
import cc.mallet.types.*

// http://wbarczynski.org/wp/2011/11/05/lda-with-mallet/
class MalletLda {
    public final static String STOP_WORDS = "data/mallet/stop-words.txt";

    /**
     * Input file Format: (document Id) \t X \t (document text)
     */
    public final static String INPUT_FILE = "data/mallet/input-eharmony.txt";
    /* in this file topics will be stored */
    public final static String TOPIC_LIST = "data/mallet/output/topics_eharmony.csv";
    /* in this file we will store data on which document belongs to which topic */
    public final static String TOPICS_PER_DOC = "data/mallet/output/topics_per_eharmony_doc.txt";
    /* number of interactions */
    public final static int NUM_OF_ITER = 50; // real app 1000 - 2000
    /* the expected number of topics */
    public final static int NUM_OF_TOPICS = 5;

    public static void main(String[] args) {
        ArrayList pipeList = new ArrayList();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new Stemify());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
        pipeList.add(new TokenSequenceRemoveStopwords(new File(STOP_WORDS), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));
        Reader fileReader = new InputStreamReader(new FileInputStream(new File(INPUT_FILE)), "UTF-8");

        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)\$"),
                3, 2, 1)); // data, label, name fields

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = NUM_OF_TOPICS;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only,
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(NUM_OF_ITER);
        model.estimate();
        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        /**
         * Get topics. Store each topic with a list of words and their weight:
         * (topic name)  - (word): (weight), ...
         */
        File topicList = new File(TOPIC_LIST);
        if (topicList.exists()) topicList.delete();

        //http://acl.ldc.upenn.edu/hlt-naacl2004/main/pdf/167_Paper.pdf
        //http://www.benmccann.com/dev-blog/latent-dirichlet-allocation-mallet/

        int i = 0;
        StringBuilder sb;
        for (TreeSet set: model.getSortedWords()) {
            sb = new StringBuilder().append(i)
            sb.append(" - ")
            for (IDSorter s: set) {
                sb.append(dataAlphabet.lookupObject(s.getID())).append(":").append(s.getWeight()).append(", ")
            }
            topicList.append(sb.append("\n").toString());
            i++;
        }

        topicList.eachLine { line -> println line }

        /**
         * Get topic per document
         */

        TopicInferencer inferencer = model.getInferencer();

        File topicsPerDoc = new File(TOPICS_PER_DOC);
        if (topicsPerDoc.exists()) topicsPerDoc.delete();

        for (int numOfInst = 0; numOfInst < instances.size(); numOfInst++) {
            StringBuilder sb1 = new StringBuilder();
            Instance inst = instances.get(numOfInst);
            double[] testProbabilities = inferencer.getSampledDistribution(inst, 10, 1, 5);
            sb1.append(inst.getName());

            for (int j = 0; j < testProbabilities.size(); j++) {
                sb1.append('\t').append(testProbabilities[j]);
            }
            topicsPerDoc.append(sb1.append('\n').toString());
        }
    }
}