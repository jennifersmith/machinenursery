package main.java;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: mneedham
 * Date: 04/11/2012
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */
public class SnowBallPlaybox {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("result: " + new QueryParser(Version.LUCENE_34, "field", new EnglishAnalyzer(Version.LUCENE_34)).parse("smiley").toString("field"));
        System.out.println("result: " + new QueryParser(Version.LUCENE_34, "field", new EnglishAnalyzer(Version.LUCENE_34)).parse("smile").toString("field"));

        System.out.println("result = " + stem("positivity"));
        System.out.println("result = " + stem("positive"));
    }

    private static String stem(String word) {
        PorterStemmer stem = new PorterStemmer();
        stem.setCurrent(word);
        stem.stem();
        return stem.getCurrent();
    }
}
