package ner_dictionary.rules;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ruleset")
public class RuleSet {
	
	private ArrayList<Rule> ruleList;

	@XmlElement(name="rule")
	public ArrayList<Rule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(ArrayList<Rule> ruleList) {
		this.ruleList = ruleList;
	}
	
}
