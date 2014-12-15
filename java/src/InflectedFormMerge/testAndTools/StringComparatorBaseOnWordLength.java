package testAndTools;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class StringComparatorBaseOnWordLength.
 */
public class StringComparatorBaseOnWordLength implements Comparator{
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object arg0, Object arg1) {
		String str1 = (String) arg0;
		String str2 = (String) arg1;
		if(str1.length()<str2.length()){
			return -1;
		}
		else if(str1.length()<str2.length()){
			return 1;
		}
		else return 0;
	}
}
