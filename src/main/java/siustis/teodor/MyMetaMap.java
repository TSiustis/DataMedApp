package siustis.teodor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;


import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.Result;
public class MyMetaMap {
	MetaMapApi api,api1;
	public static List<Result> resultList;
	ArrayList<String> diseases1 = new ArrayList<String>();
	static File file;

	String input;
	int i = 0;
	
	public MyMetaMap(File file) throws  FileNotFoundException, SAXException {

		InputStream         stream    = new FileInputStream(file);
		AutoDetectParser parser = new AutoDetectParser();
	    BodyContentHandler handler = new BodyContentHandler();
	    Metadata metadata = new Metadata();
	    try  {
	        parser.parse(stream, handler, metadata);
	        input = handler.toString();
	    }

		
	 catch (IOException e) {
		e.printStackTrace();
	} catch (TikaException e) {
		e.printStackTrace();
	}

	api = new MetaMapApiImpl();
	  api.setOptions("-y -c -D");
	    
		 resultList = api.processCitationsFromString(input);
	   
	}
	
}


	
		
		
		
	
			
			
	

		


	
	
	