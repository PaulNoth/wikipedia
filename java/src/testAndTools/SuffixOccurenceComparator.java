package testAndTools;

import java.util.Comparator;

import mergingInflectedFormNE.Suffix;

public class SuffixOccurenceComparator implements Comparator{
	public int compare(Object arg0, Object arg1) {
		Suffix sf0 = (Suffix) arg0;
		Suffix sf1 = (Suffix) arg1;
		int count1 = sf0.getNumberOfOccurences();
		int count2 = sf1.getNumberOfOccurences();
		if(count1 > count2){
			return -1;
		}
		else if(count1 < count2){
			return 1;
		}
		else return 0;
	}
}
