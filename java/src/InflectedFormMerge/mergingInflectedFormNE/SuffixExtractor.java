package mergingInflectedFormNE;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import testAndTools.SortHashMap;
import testAndTools.StringComparatorBaseOnAlphabeticalOrder;
import testAndTools.StringComparatorBaseOnWordLength;
import testAndTools.StringTools;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;


// TODO: Auto-generated Javadoc
/**
 * The Class SuffixExtractor.
 */
public class SuffixExtractor {

	/**
	 * Suffix and root change extraction.
	 *
	 * @param hmNE the hm ne
	 * @param hmSuf the hm suf
	 * @param hmRootChange the hm root change
	 */
	public void SuffixAndRootChangeExtraction(HashMap<String, NamedEntity> hmNE, HashMap<String,Suffix> hmSuf, HashMap<String,RootChange> hmRootChange){
		
		LinkedList<LinkedList<String>> IFtokeny;
		for(Map.Entry<String, NamedEntity> entry : hmNE.entrySet()){
			String key = entry.getKey();
			NamedEntity ne = entry.getValue();

			IFtokeny = extractInflectedForms(ne.getListInflectedForms());
			//printTokenIF(IFtokeny);
			obtainSuffixAndRootChangeList(IFtokeny, hmSuf, hmRootChange);	//this implementation does not allow sending number of occurrences
			
			//System.out.println("");
		}
		//Tokenizer tok = new Tokenizer();
		//LinkedList<String> tokeny = tok.tokenizeString(ne.getNE());
		
		SortHashMap sHM = new SortHashMap();
		hmSuf = sHM.sortSuffixesByNumberOfOccurrences(hmSuf);
		
		
		printListOfSuffixes(hmSuf);
		
		System.out.println("\n\n=======================================================\n\n");
		hmRootChange = sHM.sortRootChangeByNumberOfOccurrences(hmRootChange);
		//Collections.sort(llRootChange, new RootChangeOccurrenceComparator());
		printListOfRootChanges(hmRootChange);
	}

	/**
	 * Obtain suffix and root change list.
	 *
	 * @param IFtokeny the inflected forms of tokens
	 * @param hmSuf the hm suf
	 * @param hmRootChange the hm root change
	 */
	public void obtainSuffixAndRootChangeList(LinkedList<LinkedList<String>> IFtokeny, HashMap<String,Suffix> hmSuf, HashMap<String,RootChange> hmRootChange){
		String root="";
			
		for(LinkedList<String> lls : IFtokeny){
			if(lls.size()>1){
				root = lls.get(0);
				lls.remove(0);
				String vowelSufRoot=findVoWelSuffix(root);
				if(vowelSufRoot.length() > 0){	//string has vowel suffix
					addSuffix(vowelSufRoot, root, hmSuf);
				}
				String rootWithVowelSuf = root;
				root = root.substring(0, root.length()-vowelSufRoot.length());
				
				if(root.matches(".*[0-9].*")){
					//System.out.println(root);
					continue;
				}
				for(String s : lls){
					if(s.matches(".*[0-9].*")){
						//System.out.println(s );
						continue;
					}
					if(s.length() > root.length()){
						addSuffix(s.substring(root.length()), s, hmSuf);						
					}
					
					obtainRootChange(rootWithVowelSuf, s, hmRootChange);		
				}
			}
				
		}
	}
	
	/**
	 * Obtain root change.
	 *
	 * @param root the root
	 * @param s the s
	 * @param hmRootChange the hash map of root changes
	 */
	public void obtainRootChange(String root, String s, HashMap<String, RootChange> hmRootChange){	//this method could be tweek further more
		String pom1 =root;
		String pom2 = s;
		
		if(!s.equalsIgnoreCase(root)){
			if(s.length()>root.length()){
				s=s.substring(0,root.length());
				String rootVowelSuf = findVoWelSuffix(root);
				String sVowelSuf = findVoWelSuffix(s);
				if(sVowelSuf.length() > 0 && rootVowelSuf.length() > 0){
					root = root.substring(0, root.length()-rootVowelSuf.length());
					s = s.substring(0, s.length()-sVowelSuf.length());
				}
			}
			else{
				String rootVowelSuf = findVoWelSuffix(root);
				String sVowelSuf = findVoWelSuffix(s);
				
				if(sVowelSuf.length() > 0 && rootVowelSuf.length() > 0){
					root = root.substring(0, root.length()-rootVowelSuf.length());
					s = s.substring(0, s.length()-sVowelSuf.length());
				}
			}		
			
			int i=0;
			while(s.charAt(i)==root.charAt(i) && i<(s.length()-1) && i<(root.length()-1)){
				i++;
			}
			String rootBeforeChange=root.substring(i);
			String rootChange=s.substring(i);
			if(rootBeforeChange.equalsIgnoreCase(rootChange)){
				return;
			}
			
			
			addRootChange(rootBeforeChange, rootChange, hmRootChange, root, s);
			
			//System.out.println(pom1 + "		" + pom2 + "\n" + rootBeforeChange + "		" + rootChange + "\n");
		}
	}
	
	/**
	 * Find token.
	 *
	 * @param str the str
	 * @param IFtokens the i ftokens
	 * @return true, if successful
	 */
	private boolean findToken(String str, LinkedList<LinkedList<String>> IFtokens){
		AbstractStringMetric asm = new Levenshtein();
		for(LinkedList<String> lls : IFtokens){
			for(String s : lls){
				if(asm.getSimilarity(str, s) >= 0.6){		//sets threshold for strings consider as similar
					if(str.startsWith(s.substring(0,s.length()/2))){	//if first half of the similar string is same as the first half of the other string
						lls.add(str);
						return true;
					}
				}
			}
		}	
		return false;
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
	 * Adds the suffix to list of different suffixes.
	 *
	 * @param suffix the suffix
	 * @param s the string in witch suffix occured
	 * @param hmSuf the hash map of suffixes
	 */
	public void addSuffix(String suffix, String s, HashMap<String, Suffix> hmSuf){
		if(hmSuf.containsKey(suffix)){
			Suffix pomSuffix = hmSuf.get(suffix);
			pomSuffix.addOccurrence(1, s, s.substring(0,s.length()-suffix.length()));	//1 is parameter of occurences
		}
		else{
			hmSuf.put(suffix, new Suffix(suffix, s, s.substring(0,s.length()-suffix.length()), 1)); //1 is parameter of occurences
		}
	}
	
	/**
	 * Adds the root change to list of different root changes.
	 *
	 * @param rootBeforeChange the characters before change
	 * @param rootChange the the characters after change
	 * @param hmRootChange the hash map of root changes
	 * @param root the root
	 * @param str the str
	 */
	public void addRootChange(String rootBeforeChange, String rootChange, HashMap<String, RootChange> hmRootChange, String root, String str){
		if(hmRootChange.containsKey(rootBeforeChange+"_"+rootChange)){
			RootChange pomRootChange = hmRootChange.get(rootBeforeChange+"_"+rootChange);
			pomRootChange.addOccurrence(root, str, 1); 		//1 is parameter of occurences
		}
		else{
			hmRootChange.put(rootBeforeChange+"_"+rootChange, new RootChange(rootChange, rootBeforeChange, root, str, 1));	
		}
	}

	/**
	 * Find vowel suffix.
	 *
	 * @param str the string in witch search for vowel suffix happen
	 * @return the string
	 */
	public String findVoWelSuffix(String str){
		String suf="";
		Pattern pSuffix = Pattern.compile("\\b[^\\s]+?([aáäeéiíyýoóuú]+)\\b");	//regex for vowel suffix detection
		Matcher mSuffix;
		mSuffix = pSuffix.matcher(str);
		
		if(mSuffix.find()){
			//System.out.println(mSuffix.groupCount() + "	" + str + "	" + mSuffix.group(1));
			suf=mSuffix.group(1);
		}
		return suf;
	}

//===========================================================================================================================
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

	/**
	 * Prints the list of suffixes.
	 *
	 * @param hmSuf the hm suf
	 */
	public void printListOfSuffixes(HashMap<String, Suffix> hmSuf){
		for(Map.Entry<String, Suffix> entry : hmSuf.entrySet()){
			Suffix suf = entry.getValue();
			System.out.println(suf.getSuffix() + "		" + suf.getNumberOfOccurrences());
		}
	}

	/**
	 * Prints the list of root changes.
	 *
	 * @param hmRootChange the hm root change
	 */
	public void printListOfRootChanges(HashMap<String, RootChange> hmRootChange){
		for(Map.Entry<String, RootChange> entry : hmRootChange.entrySet()){
			RootChange rc = entry.getValue();
			System.out.println(rc.getRootChange() + "		" + rc.getRootBeforeChange() + "		" + rc.getNumberOfOccurrences());
		}

	}
}
