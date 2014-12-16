package wikical.dataclasses;

/**
 * Data class for saving searched data
 * @author Martin Polak
 *
 */
public class SearchData {

	private String name;
	private String birthDate;
	private String deathDate;

	public SearchData(String name, String birthDate, String deathDate) {
		this.name = name;
		this.birthDate = birthDate;
		this.deathDate = deathDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}
}
