package dataManipulation;

public class Languages {
String german;
String french;
String english;
public String getEnglish() {
	return english;
}
public void setEnglish(String english) {
	this.english = english;
}
String other;
//ci ukazuje francuzky aj na anglicky - rovnake slovo
boolean benglish = false;
//ci ukazuje neemcky aj na anglicky 
boolean bgerman = false;
//ci ukazuje francuzske slovo na nemecke
boolean bfrench = false;
//ci ukazuje nemecke na francuzske

public String getGerman() {
	return german;
}
public boolean isBenglish() {
	return benglish;
}
public void setBenglish(boolean benglish) {
	this.benglish = benglish;
}
public boolean isBgerman() {
	return bgerman;
}
public void setBgerman(boolean bgerman) {
	this.bgerman = bgerman;
}
public boolean isBfrench() {
	return bfrench;
}
public void setBfrench(boolean bfrench) {
	this.bfrench = bfrench;
}
public void setGerman(String german) {
	this.german = german;
}
public String getFrench() {
	return french;
}
public void setFrench(String french) {
	this.french = french;
}
public String getOther() {
	return other;
}
public void setOther(String other) {
	this.other = other;
}


}
