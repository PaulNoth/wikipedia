package testAndTools;

import java.util.List;

public class StringTools {

/**
 * Uniq.
 *
 * @param ls the ls
 */
public void uniq(List<String> ls){		//remove redundant multiple occurrence strings from list
	int i;								// list has to be in alphabeticla order
	for(i=0;i<(ls.size()-1);i++){
		if(ls.get(i).equals(ls.get(i+1))){
			ls.remove(i+1);
			i--;
		}
	}
}

	
}
