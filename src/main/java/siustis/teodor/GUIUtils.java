package siustis.teodor;

import java.awt.Frame;
import java.awt.Label;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.tika.Tika;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

public class GUIUtils {
	static MyMetaMap mm;
	private static boolean found;
	private static int i;
	private static InputStream file;
	public static DatabaseDAO db;
	private static Frame JFrame;
	public static List<PacientBean>pacients;
	private static boolean duplicate;
	static Connection connection ;
	static PreparedStatement ptmt ;
	static ResultSet resultSet;
	
	public static void fillTable (File file) throws Exception{
		
	mm = new MyMetaMap(file);
		 String currToken ="";	// BIO model - B - beginning simptoms - I - inside - O - outside
		 DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Name");
		model.addColumn("Diagnostic");
		model.addColumn("Symptoms");
		model.addColumn("Code");
		model.addColumn("BIO");
		model.addColumn("Other Sources");
		model.addColumn("Score");
		JTable table = new JTable();
		table.setModel(model);
		 i=0;
		 int rowModel;
		 currToken = "\\O";
		for(Result result: MyMetaMap.resultList){
			
			for (Utterance utterance: result.getUtteranceList()) {
				
				for (PCM pcm: utterance.getPCMList()) {
					found = false;
					for (Mapping map: pcm.getMappingList()) {

			    		  
						for (Ev mapEv: map.getEvList()) {
						    	  if(mapEv!=null){
						    		
						    			 
						    		  if(mapEv.getSources().contains("ICD10CM")){
						    			   if(mapEv.getSemanticTypes().toString().equalsIgnoreCase("[dsyn]") && currToken.equals("\\O") && !found){
									        	currToken = "\\B";
									        	  StringBuilder sources = new StringBuilder();
									        	for(String s: mapEv.getSources()){
										        	if(s!=""){
											        	sources.append(s);
											        	sources.append(" ");
											        	}
										        }
									        	Object[] row = {mapEv.getMatchedWords().toString(),mapEv.getConceptName().toString(),mapEv.getPreferredName(),"ICD10CM",currToken,sources.toString(),mapEv.getScore()};
									        	model.addRow(row);
									        	
									          }
								        if(mapEv.getSemanticTypes().toString().equalsIgnoreCase("[dsyn]") && currToken.equals("\\B") && !found){
								        	currToken = "\\I";
								        	  StringBuilder sources = new StringBuilder();
								        	for(String s: mapEv.getSources()){
									        	if(s!=""){
										        	sources.append(s);
										        	sources.append(" ");
										        	}
									        }
								        	Object[] row = {mapEv.getMatchedWords().toString(),mapEv.getConceptName().toString(),mapEv.getPreferredName(),"ICD10CM",currToken,sources.toString(),mapEv.getScore()};
								        	model.addRow(row);
								        	
								          }
								        else  if(mapEv.getSemanticTypes().toString().equalsIgnoreCase("[dsyn]") && currToken.equals("\\I")&& !found ){
								        	currToken="\\I";
								        	  StringBuilder sources = new StringBuilder();
								        	for(String s: mapEv.getSources()){
									        	if(s!=""){
										        	sources.append(s);
										        	sources.append(" ");
										        	}
									        }
								        	Object[] row = {mapEv.getMatchedWords().toString(),mapEv.getConceptName().toString(),mapEv.getPreferredName(),"ICD10CM",currToken,sources.toString(),mapEv.getScore()};
								        	model.addRow(row);
								        	
								          }
								        else if(mapEv.getSemanticTypes().toString().equalsIgnoreCase("[dsyn]") && currToken.equals("\\O")&& !found ){
								        	currToken = "\\B";
								        	  StringBuilder sources = new StringBuilder();
								        	for(String s: mapEv.getSources()){
									        	if(s!=""){
										        	sources.append(s);
										        	sources.append(" ");
										        	}
									        }
								        	Object[] row = {mapEv.getMatchedWords().toString(),mapEv.getConceptName().toString(),mapEv.getPreferredName(),"ICD10CM",currToken,sources.toString(),mapEv.getScore()};
								        	model.addRow(row);
								        
								 }	  
								        
								        
								       
								     
								      
								          }
						    		  else if(!mapEv.getSemanticTypes().equals("dysn"))  {
									        	currToken = "\\O";
									        	  StringBuilder sources = new StringBuilder();
									        	  
									        	for(String s: mapEv.getSources()){
										        	if(s!=""){
										        	sources.append(s);
										        	sources.append(" ");
										        	}
										        }
									        	Object[] row = {mapEv.getMatchedWords().toString(),mapEv.getConceptName().toString(),mapEv.getPreferredName(),"ICD10CM",currToken,sources.toString(),mapEv.getScore()};
									        	
									        	model.addRow(row);
									        	  }
						    	
						    		  
						      }
						    	  
							        	
						    }
						  }
						
					            
					
				}
		}
		}
		

		
	   
			 JOptionPane.showMessageDialog(null, new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	}	 

		 
	
	   public static void showText(File file) throws Exception{
		  mm = new MyMetaMap(file);
		   StringBuilder message = new StringBuilder();
			  JTextPane t = new JTextPane();
			    t.setContentType( "text/html" );
		       for(Result result: MyMetaMap.resultList){
					for (Utterance utterance: result.getUtteranceList()) {
					  for (PCM pcm: utterance.getPCMList()) {
						for (Mapping map: pcm.getMappingList()) {
						    for (Ev mapEv: map.getEvList()) {
						    	  if(mapEv!=null){
						    		  	if(mapEv.getSources().contains("ICD10CM")){
								      	if(mapEv.getSemanticTypes().toString().equalsIgnoreCase("[sosy]") || mapEv.getSemanticTypes().toString().equalsIgnoreCase("[dsyn]")){
								        	message.append("Match: " + mapEv.getMatchedWords().toString() + " ");
								        	message.append(mapEv.getPreferredName() + " ");
								        	if(mapEv.getSemanticTypes().toString().equalsIgnoreCase("[sosy]"))
								        		message.append("Type: "  + "symptom");
								        		if(mapEv.getSemanticTypes().toString().equals("[dsyn]"))
								        			message.append("Type: "  + "disease");
								                 	message.append("\n");
								        		}else{
								        			message.append("Match:" + mapEv.getMatchedWords().toString() + " ");
								        			message.append(mapEv.getPreferredName() + " ");
								        			message.append("Type: "  + mapEv.getSemanticTypes().toString()+"\n");
								        		}
						    		  	}
								 }
						    }
						  }
						}
					            
					}
				}
		        String message1 = message.toString();
		        JTextArea text1 = new JTextArea();
		        text1.setText(message1);
		        text1.setLineWrap(true);
		        text1.setEditable(false);
		        text1.setCaretPosition(0);
		        text1.setSize(300,200);
		        t.setText(message1);
		        JScrollPane scroll = new JScrollPane(text1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		        
			    JDialog dialog = new JDialog(JFrame,"Standardized text",true);
			    dialog.add(scroll);
			    dialog.pack();
			    dialog.setVisible(true);
			   
	   }
	 
	
		public static void addPacientsToDb(File file,String name) throws Exception{
			mm = new MyMetaMap(file);
			 pacients = new ArrayList<PacientBean>();
			 db = new DatabaseDAO();
			 String query="SELECT * FROM pacients";
				Connection connection = null;
				PreparedStatement ptmt = null;
				ResultSet resultSet = null;
				connection = DatabaseDAO.getConnection();
				ptmt = connection.prepareStatement(query);
				resultSet = ptmt.executeQuery();
				while (resultSet.next()) {
					if(name.equals(resultSet.getString("Document")))
						duplicate = true;
				}
		int i = 0;
		if(!duplicate){
		for(Result result : MyMetaMap.resultList){
			for (Utterance utterance: result.getUtteranceList()) {
				for (PCM pcm: utterance.getPCMList()) {
			 for (Mapping map: pcm.getMappingList()) {
	          for (Ev mapEv: map.getEvList()) {
	            if(mapEv!=null)
	            	 if(mapEv.getSources().contains("ICD10CM")){
	            	
	            	 pacients.add(new PacientBean(mapEv.getMatchedWords().toString(),mapEv.getConceptName(),mapEv.getPreferredName(),mapEv.getMatchedWords().toString()));
			        db.add(pacients.get(i), name);
			         i++;
	            	 }
	          }
	        }
	  }
			}

		}
	}
		else{
			int j = 0;
			for(Result result : MyMetaMap.resultList){
				for (Utterance utterance: result.getUtteranceList()) {
					for (PCM pcm: utterance.getPCMList()) {
				 for (Mapping map: pcm.getMappingList()) {
		          for (Ev mapEv: map.getEvList()) {
		            if(mapEv!=null)
		            	 if(mapEv.getSources().contains("ICD10CM")){
		            	
		            	 pacients.add(new PacientBean(mapEv.getMatchedWords().toString(),mapEv.getConceptName(),mapEv.getPreferredName(),mapEv.getMatchedWords().toString()));
				        db.UpdateDatabase(pacients.get(j), name,j);
				         j++;
		            	 }
		          }
		        }
		  }
				}

			}
		}
		
		}

	
	   public static void showTagger(File file) throws IOException, TikaException{
		   
		   JTextArea text1 = new JTextArea();
	       text1.setLineWrap(true);
	       text1.setEditable(false);
	       text1.setCaretPosition(0);
	       text1.setSize(300,100);
		   String continut = new Tika().parseToString(file);
		    Properties props = new Properties();

		    props.setProperty("annotators","tokenize, ssplit, pos");

		    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		    Annotation annotation = new Annotation(continut);
		    pipeline.annotate(annotation);
		    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		    for (CoreMap sentence : sentences) {
		        for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
		            String word = token.get(CoreAnnotations.TextAnnotation.class);
		            // POS al fiecarui cuvant
		            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
		            text1.append(word + "/" + pos + " ");
		          //  System.out.print(word + "/" + pos + " ");
		           
		        }
		        text1.append("");
	   }
		    JScrollPane scroll = new JScrollPane(text1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		    	       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		    JDialog dialog = new JDialog(JFrame,"Stanford NLP Tagger",true);
		    dialog.add(scroll);

		    dialog.pack();
		    dialog.setVisible(true);
	   }
	   public static void findAll() throws Exception {
		
				String queryString = "SELECT * FROM pacients "  ;
				connection = DatabaseDAO.getConnection();
				ptmt = connection.prepareStatement(queryString);
				resultSet = ptmt.executeQuery();
				 DefaultTableModel model = new DefaultTableModel();
				 
					model.addColumn("ID");
					model.addColumn("Document");
					model.addColumn("Name");
					model.addColumn("Disease");
					model.addColumn("Symptom");
					model.addColumn("Match");
					
					JTable table = new JTable();
					table.setModel(model);
				while (resultSet.next()) {
					
					Object[] row = {resultSet.getInt("ID"),resultSet.getString("Document"),resultSet.getString("Name"),resultSet.getString("Disease"),resultSet.getString("Symptom"),resultSet.getString("Matching")};
		        	
		        	model.addRow(row);
				}
				
				 JScrollPane scroll = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    	       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    JDialog dialog = new JDialog(JFrame,"Database",true);
			    dialog.add(scroll);

			    dialog.pack();
			    dialog.setVisible(true);
		   
			}
	   
	   public static void showICD() throws Exception {
			MyTable ICD10 = new MyTable();
			 DefaultTableModel model = new DefaultTableModel();
			 
				model.addColumn("Disease");
				model.addColumn("Category");
				model.addColumn("Code");
				
				JTable table = new JTable();
				table.setModel(model);
				for (String key : MyTable.ICD10.rowKeySet()) {
				    	   Map<String, String> tmp = MyTable.ICD10.row(key);
				    	   for (java.util.Map.Entry<String, String> pair : tmp.entrySet()) {
				    		   Object[] row = {key,pair.getKey(),pair.getValue()};

						    	model.addRow(row);
						    	
				    	    }
				    	 
				   }
				 
				  JScrollPane scroll = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    	       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    JDialog dialog = new JDialog(JFrame,"ICD10",true);
			    dialog.add(scroll);
			    dialog.pack();
			    dialog.setVisible(true);
			   
	   }
	   public static void showAbout() throws BadLocationException {
		   JTextPane about = new JTextPane();
       	
       	about.setContentType("text/html");
         about.setName("About Me ");
       about.setEditable(false);
	about.setCaretPosition(0);
	about.setSize(300,200);
	about.setText(
       "<html><body><p>" +
       "Author: Siustis Teodor<br />" +
       "Contact me at: " +
       "<a href='mailto:" +" siustis.teodor@gmail.com" + "?subject=About the App'>" + "siustis.teodor@gmail.com" + "</a>" +
               "<br /><br />" +
       "</p></body></html>");
        JScrollPane scroll = new JScrollPane(about,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	    	       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    JDialog dialog = new JDialog(JFrame,"About",true);
	    scroll.setSize(300,200);
	    dialog.add(scroll);
	    dialog.pack();
	    dialog.setVisible(true);
	   }
	   public static void showUsage() throws BadLocationException {
		   JTextPane about = new JTextPane();
       	
       	about.setContentType("text/html");
         about.setName("Usage");
       about.setEditable(false);
	about.setCaretPosition(0);
	about.setSize(400,400);
	about.setText(
       "<html><body><p>" +
       "Usage:Enter a file from the system using the basic file commands or write one yourself and perform any of the following actions:<br />" +
       "<br/>ICD-10 Table: Shows a table of the ICD-10 Contents<br/> "
       + "Show Table: Shows a table matching the type of diseases and the ICD10 code, if present, from the file<br/>" +
       "Add to Database:Adds information such as type of diseases, matched words etc from the file into the database<br/>" 
+ "Show Standardized Text: Displays medical information extracted using Metamap from the file<br/>"+
       "Tagger:Displays the text from the file with added Part of speech tag to each word<br/>"+
       "List Database: Displays the contents of the database<br/>"+
   "<br />" +
               "Note: The program may not read some PDFs<br />" +
          
       "</p></body></html>");
        JScrollPane scroll = new JScrollPane(about,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	    	       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scroll.setSize(300,200);
        JDialog dialog = new JDialog(JFrame,"About",true);
	    
	    dialog.add(scroll);
	    dialog.pack();
	    dialog.setVisible(true);
	   }
	   public static String parse(File file) throws IOException, MimeTypeException, SAXException, TikaException{
		   String input = "";
		   InputStream         stream    = new FileInputStream(file);
			AutoDetectParser parser = new AutoDetectParser();
		    BodyContentHandler handler = new BodyContentHandler();
		    Metadata metadata = new Metadata();
		    try  {
		        parser.parse(stream, handler, metadata);
		       return handler.toString();
		    }catch(Exception e){
		    	e.printStackTrace();
		    	return null;
		    }
			
	   }
	   
}
		
	 	   

