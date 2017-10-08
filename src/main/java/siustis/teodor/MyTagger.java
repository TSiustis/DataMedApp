
package siustis.teodor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import edu.stanford.nlp.dcoref.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import com.google.common.collect.Table;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

public class MyTagger {
	String continut;
	//POS Tagger pentru prelucrarea limbajului natural
    public MyTagger(File file) throws IOException, TikaException{
    	
    	
    	    continut = new Tika().parseToString(file);
    	    Properties props = new Properties();

    	    props.setProperty("annotators","tokenize, ssplit, pos");

    	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    	    Annotation annotation = new Annotation(continut);
    	    pipeline.annotate(annotation);
    	    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
    	    for (CoreMap sentence : sentences) {
    	        for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
    	            String word = token.get(CoreAnnotations.TextAnnotation.class);
    	            // Part of speech of every word
    	            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
    	            System.out.print(word + "/" + pos + " ");
    	           
    	        }
    	        System.out.println("");
    	    }
    	
    }
}
   
  


