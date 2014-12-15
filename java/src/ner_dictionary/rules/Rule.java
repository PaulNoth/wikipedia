package ner_dictionary.rules;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="rule")
public class Rule {
	
	private String pattern;
	private String categoryName;
	private Integer categoryId;
	private RuleType type;
	
	public Rule(){
		this.type = RuleType.NONE;
	}
	
	@XmlElement(name="pattern")
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	@XmlElement(name="category")
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String category) {
		this.categoryName = category;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	@XmlAttribute(name="type")
	public String getTypeAsString() {
		return type.toString().toLowerCase();
	}
	public void setType(String type) {
		try {	
			this.type = RuleType.valueOf(type.toUpperCase());
		} catch (IllegalArgumentException e) {
			System.err.println("Type of rule " + this.type + " is undefined.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	public boolean verifyTypeById(RuleType type) {
		return (type == this.type);
	}
}
