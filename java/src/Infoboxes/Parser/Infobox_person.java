package Parser;

import java.io.Serializable;

//trieda predstavuje infobox person so vsetkymi atributmi, obsahuje iba atributy a funkcie get, set !
public class Infobox_person implements Serializable{
	
	private String name;
	private String image;
	private String image_size;
	private String birth_date;
	private String birth_place;
	private String death_date;
	private String death_place;
	private String birth_day;
	private String birth_month;
	private String birth_year;
	private String death_day;
	private String death_month;
	private String death_year;
	
	public String getBirth_day() {
		return birth_day;
	}

	public void setBirth_day(String birth_day) {
		this.birth_day = birth_day;
	}

	public String getBirth_month() {
		return birth_month;
	}

	public void setBirth_month(String birth_month) {
		this.birth_month = birth_month;
	}
	
	public String getBirth_year() {
		return birth_year;
	}
	
	public void setBirth_year(String birth_year) {
		this.birth_year = birth_year;
	}
	
	public String getDeath_day() {
		return death_day;
	}
	
	public void setDeath_day(String death_day) {
		this.death_day = death_day;
	}
	
	public String getDeath_month() {
		return death_month;
	}
	
	public void setDeath_month(String death_month) {
		this.death_month = death_month;
	}
	
	public String getDeath_year() {
		return death_year;
	}
	
	public void setDeath_year(String death_year) {
		this.death_year = death_year;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImage_size() {
		return image_size;
	}
	
	public void setImage_size(String image_size) {
		this.image_size = image_size;
	}
	
	public String getBirth_date() {
		return birth_date;
	}
	
	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}
	
	public String getBirth_place() {
		return birth_place;
	}
	
	public void setBirth_place(String birth_place) {
		this.birth_place = birth_place;
	}
	
	public String getDeath_date() {
		return death_date;
	}
	
	public void setDeath_date(String death_date) {
		this.death_date = death_date;
	}
	
	public String getDeath_place() {
		return death_place;
	}
	
	public void setDeath_place(String death_place) {
		this.death_place = death_place;
	}

}
