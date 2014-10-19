package mergingInflectedFormNE;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import testAndTools.StringComparatorBaseOnAlphabeticalOrder;
import testAndTools.StringTools;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

// TODO: Auto-generated Javadoc
/**
 * The Class NamedEntity.
 */
public class NamedEntity {

/** The ne. */
private String ne;

/** The inflected forms. */
private LinkedList<String> inflectedForms;
	
/**
 * Instantiates a new named entity.
 *
 * @param ne the ne
 */
public NamedEntity(String ne){
	this.ne=ne;
	inflectedForms = new LinkedList<String>();
	this.inflectedForms.add(ne);
}

/**
 * Adds the inflected form.
 *
 * @param neIF the ne if
 */
public void addInflectedForm(String neIF){
	this.inflectedForms.add(neIF);
	
	Collections.sort(inflectedForms, new StringComparatorBaseOnAlphabeticalOrder());
	StringTools st = new StringTools();
	st.uniq(inflectedForms);
}

/**
 * Compare.
 *
 * @param s the s
 * @return the double
 */
public double compare(String s){		//nie je dokoncene cast implementacie je v stringWordSeparator
	//String[] pom = s.split("\\s?(\\b\\p{L}+?\\b)\\s?");
	//for(String i : pom){
	//	System.out.println(i);
	//}
	
	double maxSimilarity = 0;
	double pomSimilarity;
	AbstractStringMetric metric = new Levenshtein();
	for(String str : this.inflectedForms){
		pomSimilarity = metric.getSimilarity(s, str);
		if(pomSimilarity > maxSimilarity){
			maxSimilarity = pomSimilarity;
		}
	}	
	return maxSimilarity;

	//return 0; 
}

//--------------------------------------------------------------------------------------------------------

/**
 * String word separator.
 *
 * @param s the s
 * @return the linked list
 */
public LinkedList<String> stringWordSeparator(String s){
	LinkedList<String> wordList = new LinkedList<String>();
	Pattern pWord = Pattern.compile("\\s?(\\b\\p{L}+?\\b)\\s?");	//regex for word detection
	Matcher mWord;
	mWord = pWord.matcher(s);
	while(mWord.find()){
		wordList.add(mWord.group(1));
		//System.out.println(mWord.group(1));
	}
	return wordList;
}

//--------------------------------------------------------------------------------------------------------

/**
 * Gets the Named Entity.
 *
 * @return the Named Entity
 */
public String getNE(){
	return this.ne;
}

/**
 * Gets the inflected forms.
 *
 * @return the inflected forms
 */
public LinkedList<String> getInflectedForms(){
	return this.inflectedForms;
}

}
