package enwikiPagerank.mapreduce;
/*
 * this is data object class
 * holds the key - namespace#nameOfPage
 * and value - list of strings - each string is one ID which pointing to namespace#nameOfPage
 */
import java.util.ArrayList;
import java.util.List;

public class PagelinksMap {

	public String key = " ";
	public List<String> value = new ArrayList<String>();
	
	
}
