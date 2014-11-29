package testAndTools;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class StringComparatorBaseOnAlphabeticalOrder.
 */
public class StringComparatorBaseOnAlphabeticalOrder implements Comparator{
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object arg0, Object arg1) {
		String str1 = (String) arg0;
		String str2 = (String) arg1;
		if(str1.compareTo(str2)<0){
			return -1;
		}
		else if(str1.compareTo(str2)>0){
			return 1;
		}
		else return 0;
	}
}
