package mergingInflectedFormNE;

// TODO: Auto-generated Javadoc
/**
 * The Class Forms.
 */
public class Forms {

	/** The form1. */
	private String form1;
	
	/** The form2. */
	private String form2;
	
	/** The occurrences. */
	private int occurrences;
	
	/**
	 * Instantiates a new forms.
	 *
	 * @param s1 the s1
	 * @param s2 the s2
	 * @param occurrence the occurrence
	 */
	public Forms(String s1, String s2, int occurrence){
		this.form1 = s1;
		this.form2 = s2;
		occurrences = occurrence;
	}
	
	/**
	 * Sets the occurrences.
	 *
	 * @param occurrence the new occurrences
	 */
	public void setOccurrences(int occurrence){
		this.occurrences = this.occurrences + occurrence;
	}
	
	/**
	 * Gets the form1.
	 *
	 * @return the form1
	 */
	public String getForm1(){
		return form1;	
	}
	
	/**
	 * Gets the form2.
	 *
	 * @return the form2
	 */
	public String getForm2(){
		return form2;	
	}
	
	/**
	 * Gets the occurrences.
	 *
	 * @return the occurrences
	 */
	public int getOccurrences(){
		return this.occurrences;
	}
}
