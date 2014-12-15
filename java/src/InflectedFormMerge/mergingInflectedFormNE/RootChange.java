package mergingInflectedFormNE;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class RootChange.
 */
public class RootChange {

/** The root change. */
private String rootChange;

/** The before root change. */
private String beforeRootChange;

/** The number of occurrences. */
private int numberOfOccurrences;

/** The hm forms. */
private HashMap<String, Forms> hmForms = new HashMap<String, Forms>();

/**
 * Instantiates a new root change.
 *
 * @param rootChange the root change
 * @param rootBeforeChange the root before change
 * @param form1 the form1
 * @param form2 the form2
 * @param occurrences the occurrences
 */
public RootChange(String rootChange, String rootBeforeChange, String form1, String form2, int occurrences){
	this.rootChange = rootChange;
	this.beforeRootChange = rootBeforeChange;

	this.numberOfOccurrences = occurrences;
	
	hmForms.put(form1+form2, new Forms(form1, form2, occurrences));
	
}

/**
 * Instantiates a new root change.
 *
 * @param rootChange2 the root change2
 * @param rootBeforeChange the root before change
 */
public RootChange(String rootChange2, String rootBeforeChange) {
	this.rootChange = rootChange2;
	this.beforeRootChange = rootBeforeChange;
	this.numberOfOccurrences = 0;
}

/**
 * Compare root change.
 *
 * @param root the root
 * @param changedRoot the changed root
 * @return true, if successful
 */
public boolean compareRootChange(String root, String changedRoot){
	if(changedRoot.equalsIgnoreCase(this.rootChange) && root.equalsIgnoreCase(this.beforeRootChange)){
		return true;				
	}
	else{
		return false;
	}
}

/**
 * Adds the occurrence.
 *
 * @param form1 the form1
 * @param form2 the form2
 * @param occurrence the occurrence
 */
public void addOccurrence(String form1, String form2, int occurrence){
	this.numberOfOccurrences = this.numberOfOccurrences + occurrence;
	
	if(hmForms.containsKey(form1+form2)){
		Forms pomF = hmForms.get(form1+form2);
		pomF.setOccurrences(occurrence);
	}
	else{
		hmForms.put(form1+form2, new Forms(form1, form2, occurrence));
	}	
}

/**
 * Gets the root change.
 *
 * @return the root change
 */
public String getRootChange(){
	return this.rootChange;
}

/**
 * Gets the root before change.
 *
 * @return the root before change
 */
public String getRootBeforeChange(){
	return this.beforeRootChange;
}

/**
 * Gets the number of occurrences.
 *
 * @return the number of occurrences
 */
public int getNumberOfOccurrences(){
	return this.numberOfOccurrences;
}

/**
 * Gets the hash map forms.
 *
 * @return the hash map forms
 */
public HashMap<String, Forms> getHashMapForms(){
	return this.hmForms;
}

}
