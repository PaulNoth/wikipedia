package mergingInflectedFormNE;

import java.util.LinkedList;

public class InflectedForm {

private String inflectedForm;
private int numberOfOccurences;

private LinkedList<String> tokens;

public InflectedForm(String iF){
	this.inflectedForm=iF;
	numberOfOccurences=1;
	
	Tokenizer t = new Tokenizer();
	tokens = t.tokenizeString(iF);
}
	
public void addOccurence(){
	this.numberOfOccurences++;
}

public void setOccurence(int o){
	this.numberOfOccurences=o;
}

public int getOccurances(){
	return this.numberOfOccurences;
}

public String getInflectedForm(){
	return this.inflectedForm;
}

public LinkedList<String> getTokens(){
	return this.tokens;
}
	
}
