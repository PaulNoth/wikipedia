package ttltoxml;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.PipedRDFIterator;
import org.apache.jena.riot.lang.PipedRDFStream;
import org.apache.jena.riot.lang.PipedTriplesStream;

import com.hp.hpl.jena.graph.Triple;

/**
 * @author Skrisa Július
 *
 *	This class uses Jena library to parse data from specified ttl files. 
 */
public class DataParserOfTtlDumps {
	private String fileToParse;
	PipedRDFStream<Triple> inputStream;
	String file;
	PipedRDFIterator<Triple> iter;
	ExecutorService executor;
	
	public DataParserOfTtlDumps(String files, String test){
		fileToParse = files;
	}
	
	public DataParserOfTtlDumps(String file1){ //creating input stream to parse by records in ttl file
		iter = new PipedRDFIterator<Triple>();
		inputStream = new PipedTriplesStream(iter);
		file = System.getProperty("user.dir")+ "\\data\\" + file1;
		
		Runnable parser = new Runnable() {
			@Override
			public void run() {
				RDFDataMgr.parse(inputStream, file);
			}
		};
		executor = Executors.newSingleThreadExecutor();
		executor.submit(parser);
	}
	
	
	public RecordModel Parse(){

		
		Triple next = iter.next(); // another record in file
		
		RecordModel record = new RecordModel(); 
		if (record.setAttributes(next))
			return record;

		return null;
				  
	}
	
	public boolean hasNext(){
		return iter.hasNext();
	}
	
	public List<RecordModel> ParseTest(){ //parser used for test same function but different return value to sipmlify test.
		
		
		final String file = System.getProperty("user.dir")+ "/data/" + fileToParse;
		PipedRDFIterator<Triple> iter = new PipedRDFIterator<Triple>();
		final PipedRDFStream<Triple> inputStream = new PipedTriplesStream(iter);
		
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Runnable parser = new Runnable() {
			@Override
			public void run() {
				RDFDataMgr.parse(inputStream, file);
			}
		};
		executor.submit(parser);

		List<RecordModel> list = new ArrayList<RecordModel>();
		while (iter.hasNext()) {
			Triple next = iter.next();
			
			RecordModel record = new RecordModel(); 
			if (record.setAttributes(next))
				list.add(record);
			
		}
		return list;
				  
	}
	
}
