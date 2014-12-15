package Parser;

import java.io.Serializable;
import java.util.List;

//trieda predstavuje infobox country so vsetkymi atributmi, obsahuje iba atributy a funkcie get, set !
public class Infobox_country implements Serializable{
	
	private String title;
	private String common_name;
	private String image_flag;
	private String image_coat;
	private String capital;
	private String[] official_religion;
	private String[] official_languages;
	private String[] government_type;
	private String area_km2;
	private String area_sq_mi;
	private String population_estimate;
	private String population_estimate_rank;
	private String currency;
	private String currency_code;

	public String[] getOfficial_religion() {
		return official_religion;
	}

	public void setOfficial_religion(String[] official_religion) {
		this.official_religion = official_religion;
	}

	public String[] getOfficial_languages() {
		return official_languages;
	}

	public void setOfficial_languages(String[] official_languages) {
		this.official_languages = official_languages;
	}

	public String[] getGovernment_type() {
		return government_type;
	}

	public void setGovernment_type(String[] government_type) {
		this.government_type = government_type;
	}

	public String getArea_km2() {
		return area_km2;
	}

	public void setArea_km2(String area_km2) {
		this.area_km2 = area_km2;
	}

	public String getArea_sq_mi() {
		return area_sq_mi;
	}

	public void setArea_sq_mi(String area_sq_mi) {
		this.area_sq_mi = area_sq_mi;
	}

	public String getPopulation_estimate() {
		return population_estimate;
	}

	public void setPopulation_estimate(String population_estimate) {
		this.population_estimate = population_estimate;
	}

	public String getPopulation_estimate_rank() {
		return population_estimate_rank;
	}

	public void setPopulation_estimate_rank(String population_estimate_rank) {
		this.population_estimate_rank = population_estimate_rank;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getImage_coat() {
		return image_coat;
	}

	public void setImage_coat(String image_coat) {
		this.image_coat = image_coat;
	}

	public String getImage_flag() {
		return image_flag;
	}

	public void setImage_flag(String image_flag) {
		this.image_flag = image_flag;
	}

	public String getCommon_name() {
		return common_name;
	}

	public void setCommon_name(String common_name) {
		this.common_name = common_name;
	}

	public String getTitle() {
	       return title;
	 }
	
	public void setTitle(String a) {
	        this.title = a;
	 }

	
}
