package mergingInflectedFormNE;

import java.util.LinkedList;

public class Suffix {

private String suffix;
private int numberOfOccurences;

private LinkedList<String> stringsWithSuffix;
	
public Suffix(String suf, String str){
	this.suffix = suf;
	this.numberOfOccurences = 1;
	
	stringsWithSuffix = new LinkedList<String>(); 
	stringsWithSuffix.add(str);
}

public boolean compareSuffix(String s){
	if(s.equalsIgnoreCase(this.suffix)){
		return true;				
	}
	else{
		return false;
	}
}

public void addOccurence(int o, String str){
	this.numberOfOccurences= this.numberOfOccurences + o;
	
	for(String s : stringsWithSuffix){
		if(s.equals(str)){
			return;
		}
	}
	stringsWithSuffix.add(str);
}

public void addOccurence(String str){
	this.numberOfOccurences++;
	
	for(String s : stringsWithSuffix){
		if(s.equals(str)){
			return;
		}
	}
	stringsWithSuffix.add(str);
}

public String getSuffix(){
	return this.suffix;
}

public int getNumberOfOccurences(){
	return this.numberOfOccurences;
}

public LinkedList<String> getStringsWithSuffix(){
	return this.stringsWithSuffix;
}
}
