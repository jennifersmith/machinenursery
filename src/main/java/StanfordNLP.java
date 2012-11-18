package main.java;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: mneedham
 * Date: 04/11/2012
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class StanfordNLP {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        String text = "positive";
        Annotation document = pipeline.process(text);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                System.out.println("lemmatized version :" + lemma);
            }
        }

    }
}

