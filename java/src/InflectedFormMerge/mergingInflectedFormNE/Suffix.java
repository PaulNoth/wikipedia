package mergingInflectedFormNE;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class Suffix.
 */
public class Suffix {

/** The suffix. */
private String suffix;

/** The number of occurences. */
private int numberOfOccurences;

/** The hm forms. */
private HashMap<String, Forms> hmForms = new HashMap<String, Forms>();

/**
 * Instantiates a new suffix.
 *
 * @param suf the suf
 * @param str1 the str1
 * @param str2 the str2
 * @param occurrence the occurrence
 */
public Suffix(String suf, String str1, String str2, int occurrence){
	this.suffix = suf;
	this.numberOfOccurences = occurrence;
	hmForms.put(str1+str2, new Forms(str1, str2, occurrence));
}

/**
 * Instantiates a new suffix.
 *
 * @param suf the suf
 */
public Suffix(String suf){
	this.suffix = suf;
	numberOfOccurences=0;
}

/**
 * Compare suffix.
 *
 * @param s the s
 * @return true, if successful
 */
public boolean compareSuffix(String s){
	if(s.equalsIgnoreCase(this.suffix)){
		return true;				
	}
	else{
		return false;
	}
}

/**
 * Adds the occurrence.
 *
 * @param o the o
 * @param str1 the str1
 * @param str2 the str2
 */
public void addOccurrence(int o, String str1, String str2){
	this.numberOfOccurences= this.numberOfOccurences + o;
	
	if(hmForms.containsKey(str1+str2)){
		Forms pomF = hmForms.get(str1+str2);
		pomF.setOccurrences(o);
	}
	else{
		hmForms.put(str1+str2, new Forms(str1, str2, o));
	}	
}

/**
 * Gets the suffix.
 *
 * @return the suffix
 */
public String getSuffix(){
	return this.suffix;
}

/**
 * Gets the number of occurrences.
 *
 * @return the number of occurrences
 */
public int getNumberOfOccurrences(){
	return this.numberOfOccurences;
}

/**
 * Gets the strings with suffix.
 *
 * @return the strings with suffix
 */
public HashMap<String,Forms> getStringsWithSuffix(){
	return this.hmForms;
}
}

