package mergingInflectedFormNE;

import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class InflectedForm.
 */
public class InflectedForm {
	
	/** The inflected form. */
	private String inflectedForm;
	
	/** The number of occurrences. */
	private int numberOfOccurrences;

	/** The tokens. */
	private LinkedList<String> tokens;

	/**
	 * Instantiates a new inflected form.
	 *
	 * @param iF the i f
	 */
	public InflectedForm(String iF){
		this.inflectedForm=iF;
		numberOfOccurrences=1;
		
		Tokenizer t = new Tokenizer();
		tokens = t.tokenizeString(iF);
	}
		
	/**
	 * Instantiates a new inflected form.
	 *
	 * @param iF the i f
	 * @param occurrences the occurrences
	 */
	public InflectedForm(String iF, int occurrences){
		this.inflectedForm=iF;
		numberOfOccurrences=occurrences;
		
		Tokenizer t = new Tokenizer();
		tokens = t.tokenizeString(iF);
	}
	
	/**
	 * Adds the occurrence.
	 */
	public void addOccurrence(){
		this.numberOfOccurrences++;
	}

	/**
	 * Sets the occurrence.
	 *
	 * @param o the new occurrence
	 */
	public void setOccurrence(int o){
		this.numberOfOccurrences=o;
	}

	/**
	 * Gets the occurrences.
	 *
	 * @return the occurrences
	 */
	public int getOccurrences(){
		return this.numberOfOccurrences;
	}

	/**
	 * Gets the inflected form.
	 *
	 * @return the inflected form
	 */
	public String getInflectedForm(){
		return this.inflectedForm;
	}

	/**
	 * Gets the tokens.
	 *
	 * @return the tokens
	 */
	public LinkedList<String> getTokens(){
		return this.tokens;
	}
		
}
