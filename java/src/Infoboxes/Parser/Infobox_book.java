package Parser;

import java.io.Serializable;
import java.util.Arrays;

//trieda predstavuje infobox book so vsetkymi atributmi, obsahuje iba atributy a funkcie get, set !
public class Infobox_book implements Serializable {
	private String name;
	private String translator;
	private String image;
	private String caption;
	private String author;
	private String country;
	private String language;
	private String subject;
	private String[] genre;
	private String published;

	private String pages;
	
	public String[] getGenre() {
		return genre;
	}
	
	public void setGenre(String[] genre) {
		this.genre = genre;
	}
	
	private String isbn;
	private String preceded_by;
	private String followed_by;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getTranslator() {
		return translator;
	}
	
	public void setTranslator(String translator) {
		this.translator = translator;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	public String getPublished() {
		return published;
	}
	
	public void setPublished(String published) {
		this.published = published;
	}
	
	
	public String getPages() {
		return pages;
	}
	
	public void setPages(String pages) {
		this.pages = pages;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	public String getPreceded_by() {
		return preceded_by;
	}
	
	public void setPreceded_by(String preceded_by) {
		this.preceded_by = preceded_by;
	}
	
	public String getFollowed_by() {
		return followed_by;
	}
	
	public void setFollowed_by(String followed_by) {
		this.followed_by = followed_by;
	}
	
	
}
