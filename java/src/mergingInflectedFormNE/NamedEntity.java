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

/** The inflected forms with occurencess count. */
private LinkedList<InflectedForm> listInflectedForm;

///** The list of inflected forms for each token */
//private LinkedList<LinkedList<String>> IFtokeny;
/**
 * Instantiates a new named entity.
 * Use for ne extractin and mergeing purposes
 *
 * @param ne the ne
 */
public NamedEntity(String ne){
	this.ne=ne;
	
	listInflectedForm = new LinkedList<InflectedForm>();
	//this.listInflectedForm.add(new InflectedForm(ne));
		
	addInflectedFormInList(ne,ne);
	
	//IFtokeny = new LinkedList<LinkedList<String>>();
}

public NamedEntity(String ne, LinkedList<InflectedForm> IF){
	this.ne=ne;
	listInflectedForm = IF;
}
/**
 * Instantiates a new named entity.
 *
 * @param la the la
 */
public NamedEntity(LinkAnchor la){
	this.ne=la.getLink();
	listInflectedForm = new LinkedList<InflectedForm>();

	if(la.getAnchor().equals("")){
		listInflectedForm.add(new InflectedForm(la.getCleanLink()));
	}
	else{
		listInflectedForm.add(new InflectedForm(la.getAnchor()));
		listInflectedForm.add(new InflectedForm(la.getCleanLink()));
	}
}

/**
 * Adds the inflected form.
 *
 * @param neIF the ne if
 */
public void addInflectedForm(String neIF, String ne){
	addInflectedFormInList(neIF,ne);
}

private void addInflectedFormInList(String neIF, String ne){
	if(neIF.equals("")){
		neIF=ne;
	}
	for(InflectedForm IF : listInflectedForm){
		if(neIF.equals(IF.getInflectedForm())){
			//System.out.println(IF.getInflectedForm() +" 	 " + neIF);
			IF.addOccurence();
			return;
		}
	}
	//System.out.println(ne +" 	 " + neIF);
	this.listInflectedForm.add(new InflectedForm(neIF));
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
	for(InflectedForm IF : this.listInflectedForm){
		pomSimilarity = metric.getSimilarity(s, IF.getInflectedForm());
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

public boolean equals(String neCandidate){
	if(neCandidate.equals(this.ne)){
		return true;
	}
	return false;
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

public LinkedList<InflectedForm> getListInflectedForms(){
	return this.listInflectedForm;
}

}
