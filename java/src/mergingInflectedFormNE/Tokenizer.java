package mergingInflectedFormNE;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import testAndTools.StringComparatorBaseOnAlphabeticalOrder;

public class Tokenizer {

public LinkedList<String> tokenizeString(String str){
LinkedList<String> llS = new LinkedList<String>();
//	([\\p{L}0-9])
Pattern pToken = Pattern.compile("([\\p{L}0-9]+)");	//regex for token separation
Matcher mToken;
mToken = pToken.matcher(str);
while(mToken.find()){
	llS.add(mToken.group(1));	
}
Collections.sort(llS, new StringComparatorBaseOnAlphabeticalOrder());

return llS;
}

public String tokensToString (LinkedList<String> lls){
	String str="";
	for(String s : lls){
		str=str+s+"\n";
	}
	return str;
}
	
}
