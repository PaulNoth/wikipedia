package wikical.dataclasses;

/**
 * Data class for saving parsed data
 * @author Martin Polak
 *
 */
public class DeathBirthParsedData {

	public String birthDate = null;
	public String deathDate = null;

	/**
	 * Save birth and death date
	 * @param birthDate
	 * @param deathDate
	 */
	public DeathBirthParsedData(String birthDate, String deathDate) {
		this.birthDate = birthDate;
		this.deathDate = deathDate;
	}
	
	/**
	 * Save birth date
	 * @param birthDate - String which will be saved
	 */
	public DeathBirthParsedData(String birthDate) {
		this.birthDate = birthDate;
	}

	/* Empty constructor (for creating just some of the data) */
	public DeathBirthParsedData() {}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}

}
