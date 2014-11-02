package mergingInflectedFormNE;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import testAndTools.StringComparatorBaseOnAlphabeticalOrder;
import testAndTools.StringComparatorBaseOnWordLength;
import testAndTools.StringTools;
import testAndTools.SuffixOccurenceComparator;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

// TODO: Auto-generated Javadoc
/**
 * The Class SuffixExtractor.
 */
public class SuffixExtractor {

	
/**
 * Suffix extraction.
 *
 * @param llNE the ll ne
 * @param llSuf the ll suf
 */
public void SuffixExtraction(LinkedList<NamedEntity> llNE, LinkedList<Suffix> llSuf){
	
	LinkedList<LinkedList<String>> IFtokeny;
	for(NamedEntity ne: llNE){
		IFtokeny = extractInflectedForms(ne.getListInflectedForms());
		//printTokenIF(IFtokeny);
		obtainSuffixList(IFtokeny, llSuf);
		//System.out.println("");
	}
	//Tokenizer tok = new Tokenizer();
	//LinkedList<String> tokeny = tok.tokenizeString(ne.getNE());
	
	Collections.sort(llSuf, new SuffixOccurenceComparator());
	printListOfSuffixes(llSuf);
}

/**
 * Obtain suffix list.
 *
 * @param IFtokeny the i ftokeny
 * @param llSuf the ll suf
 */
public void obtainSuffixList(LinkedList<LinkedList<String>> IFtokeny, LinkedList<Suffix> llSuf){
	String root="";
		
	for(LinkedList<String> lls : IFtokeny){
		if(lls.size()>1){
			root = lls.get(0);
			lls.remove(0);
			String vowelSufRoot=findVoWelSuffix(root);
			if(vowelSufRoot.length() > 0){	//string has vowel suffix
				addSuffix(vowelSufRoot, root, llSuf);
			}
			root = root.substring(0, root.length()-vowelSufRoot.length());
			
			for(String s : lls){
				if(s.length() > root.length()){
					addSuffix(s.substring(root.length()), s, llSuf);					
				}
			}
		}
			
	}
}

/**
 * Adds the suffix to list of different suffixes.
 *
 * @param suffix the suffix
 * @param s the s
 * @param llSuf the ll suf
 */
public void addSuffix(String suffix, String s, LinkedList<Suffix> llSuf){
for(Suffix suf : llSuf){
	if(suf.compareSuffix(suffix)){
		suf.addOccurence(s);
		return;
	}
}
llSuf.add(new Suffix(suffix, s));
}

/**
 * Find vo wel suffix.
 *
 * @param str the str
 * @return the string
 */
public String findVoWelSuffix(String str){
	String suf="";
	Pattern pSuffix = Pattern.compile("\\b[^\\s]+?([aáäeéiíyýoóu]+)\\b");	//regex for vowel suffix detection
	Matcher mSuffix;
	mSuffix = pSuffix.matcher(str);
	
	if(mSuffix.find()){
		//System.out.println(mSuffix.groupCount() + "	" + str + "	" + mSuffix.group(1));
		suf=mSuffix.group(1);
	}
	return suf;
}

/**
 * Extract inflected forms.
 *
 * @param llIF the ll if
 * @return the linked list
 */
public LinkedList<LinkedList<String>> extractInflectedForms(LinkedList<InflectedForm> llIF){
	LinkedList<LinkedList<String>> IFtokeny = new LinkedList<LinkedList<String>>();
	
	for(InflectedForm IF : llIF){
		for(String s : IF.getTokens()){
			//if(s.matches("[\\p{Lu}0-9.]+$")){	//matches initials or numbers
			//	continue;
			//}
/*to lower case*/s=s.toLowerCase();	
			if(IFtokeny.isEmpty()){
				LinkedList<String> pomL = new LinkedList<String>();
				pomL.add(s);
				IFtokeny.add(pomL);
			}
			else{
				if(!findToken(s, IFtokeny)){
					LinkedList<String> pomL = new LinkedList<String>();
					pomL.add(s);
					IFtokeny.add(pomL);
				}
			}
		}
	}
	
	for(LinkedList<String> lls : IFtokeny){
		Collections.sort(lls, new StringComparatorBaseOnAlphabeticalOrder());
		StringTools st = new StringTools();
		st.uniq(lls);
		Collections.sort(lls, new StringComparatorBaseOnWordLength());								  
	}
	return IFtokeny;
}

/**
 * Find token.
 *
 * @param str the str
 * @param IFtokeny the i ftokeny
 * @return true, if successful
 */
private boolean findToken(String str, LinkedList<LinkedList<String>> IFtokeny){
	AbstractStringMetric asm = new Levenshtein();
	for(LinkedList<String> lls : IFtokeny){
		for(String s : lls){
			if(asm.getSimilarity(str, s) >= 0.6){
				if(str.startsWith(s.substring(0,s.length()/2))){
					lls.add(str);
					return true;
				}
			}
		}
	}	
	return false;
}

/**
 * Prints the token if.
 *
 * @param tokenIF the token if
 */
public void printTokenIF(LinkedList<LinkedList<String>> tokenIF){
	for(LinkedList<String> lls : tokenIF){
		for(String s : lls){
			System.out.print(s + "    ");
		}
		System.out.println();
	}
}

public void printListOfSuffixes(LinkedList<Suffix> llSuf){
	for(Suffix suf : llSuf){
		System.out.println(suf.getSuffix() + "		" + suf.getNumberOfOccurences());
	}
}
	
}
