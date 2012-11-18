package main.java;

import org.yawni.wordnet.*;

import java.io.IOException;
import java.util.List;

public class WordNet {
    public static void main(String[] args) throws IOException {
        org.yawni.wordnet.WordNet instance = org.yawni.wordnet.WordNet.getInstance();

        Word positive = instance.lookupWord("positive", POS.ADJ);

        List<Relation> relations = positive.getWordSenses().get(0).getRelations(RelationType.DERIVATIONALLY_RELATED);

        for (Relation relation : relations) {
            System.out.println("relation = " + relation);
        }

        System.out.println("positive = " + positive.getSynsets());

        for (Synset synset : positive.getSynsets()) {
            List<WordSense> wordSenses = synset.getWordSenses();

            for (WordSense wordSense : wordSenses) {
                System.out.println("wordSense.getDescription() = " + wordSense.getDescription());
            }

//            String description = synset.getDescription();
//            System.out.println("description = " + description);
//            System.out.println("synset = " + synset);
        }


    }
}
