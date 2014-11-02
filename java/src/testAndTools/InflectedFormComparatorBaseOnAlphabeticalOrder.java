package testAndTools;

import java.util.Comparator;

import mergingInflectedFormNE.InflectedForm;

public class InflectedFormComparatorBaseOnAlphabeticalOrder implements Comparator{

	public int compare(Object arg0, Object arg1) {
		InflectedForm if0 = (InflectedForm) arg0;
		InflectedForm if1 = (InflectedForm) arg1;
		String str1 = (String) if0.getInflectedForm();
		String str2 = (String) if1.getInflectedForm();
		if(str1.compareTo(str2)<0){
			return -1;
		}
		else if(str1.compareTo(str2)>0){
			return 1;
		}
		else return 0;
	}
	
}
