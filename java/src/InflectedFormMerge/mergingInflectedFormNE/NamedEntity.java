package mergingInflectedFormNE;

import java.util.LinkedList;

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

	/**  The list of inflected forms for each token. */
	private LinkedList<LinkedList<String>> IFtokens;

	/**
	 * Instantiates a new named entity.
	 *
	 * @param la the la
	 */
	public NamedEntity(LinkAnchor la){
		this.ne=la.getLink();
		listInflectedForm = new LinkedList<InflectedForm>();

		if(la.getAnchor().equals("")){
			listInflectedForm.add(new InflectedForm(la.getCleanLink(),la.getOccurrences()));
		}
		else{
			listInflectedForm.add(new InflectedForm(la.getAnchor(),la.getOccurrences()));
			listInflectedForm.add(new InflectedForm(la.getCleanLink(),la.getOccurrences()));
		}
	}

	/**
	 * Instantiates a new named entity.
	 *
	 * @param neName the ne name
	 * @param iF the i f
	 */
	public NamedEntity(String neName, LinkedList<InflectedForm> iF) {
		listInflectedForm = iF;
		this.ne=neName;
	}

	/**
	 * Adds the inflected form in list.
	 *
	 * @param neIF the ne if
	 * @param ne the ne
	 * @param occurrences the occurrences
	 */
	public void addInflectedFormInList(String neIF, String ne, int occurrences){
		if(neIF.equals("")){
			neIF=ne;
		}
		for(InflectedForm IF : listInflectedForm){
			if(neIF.equals(IF.getInflectedForm())){
				//System.out.println(IF.getInflectedForm() +" 	 " + neIF);
				IF.addOccurrence();
				return;
			}
		}
		//System.out.println(ne +" 	 " + neIF);
		this.listInflectedForm.add(new InflectedForm(neIF,occurrences));
	}
	//--------------------------------------------------------------------------------------------------------
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
	
	/**
	 * Compare.
	 *
	 * @param s the s
	 * @return the double
	 */
	public double compare(String s){		//not finish, part of the implementation is in stringWordSeparator
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
	
	/**
	 * Adds the inflected form.
	 *
	 * @param neIF the ne if
	 * @param ne the ne
	 */
	public void addInflectedForm(String neIF, String ne){
		addInflectedFormInList(neIF,ne);
	}

	/**
	 * Adds the inflected form in list.
	 *
	 * @param neIF the ne if
	 * @param ne the ne
	 */
	private void addInflectedFormInList(String neIF, String ne){
		if(neIF.equals("")){
			neIF=ne;
		}
		for(InflectedForm IF : listInflectedForm){
			if(neIF.equals(IF.getInflectedForm())){
				//System.out.println(IF.getInflectedForm() +" 	 " + neIF);
				IF.addOccurrence();
				return;
			}
		}
		//System.out.println(ne +" 	 " + neIF);
		this.listInflectedForm.add(new InflectedForm(neIF));
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
	 * Gets the list inflected forms.
	 *
	 * @return the list inflected forms
	 */
	public LinkedList<InflectedForm> getListInflectedForms(){
		return this.listInflectedForm;
	}

	/**
	 * Gets the i ftokens.
	 *
	 * @return the i ftokens
	 */
	public LinkedList<LinkedList<String>> getIFtokens(){
		return this.getIFtokens();
	}
}
