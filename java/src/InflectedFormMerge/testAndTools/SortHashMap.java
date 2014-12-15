package testAndTools;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mergingInflectedFormNE.Forms;
import mergingInflectedFormNE.RootChange;
import mergingInflectedFormNE.Suffix;

// TODO: Auto-generated Javadoc
/**
 * The Class SortHashMap.
 */
public class SortHashMap {

	/**
	 * Sort suffixes by number of occurrences.
	 *
	 * @param hmSuf the hm suf
	 * @return the hash map
	 */
	public HashMap<String, Suffix> sortSuffixesByNumberOfOccurrences(HashMap<String, Suffix> hmSuf){
		List<Map.Entry<String, Suffix>> entries = new LinkedList<Map.Entry<String, Suffix>>((Collection<? extends Entry<String, Suffix>>) hmSuf.entrySet());
		
		Collections.sort(entries, new Comparator<Map.Entry<String, Suffix>>(){

			@Override
			public int compare(Entry<String, Suffix> arg0, Entry<String, Suffix> arg1) {
				Suffix sf0 = (Suffix) arg0.getValue();
				Suffix sf1 = (Suffix) arg1.getValue();
				int count1 = sf0.getNumberOfOccurrences();
				int count2 = sf1.getNumberOfOccurrences();
				if(count1 > count2){
					return -1;
				}
				else if(count1 < count2){
					return 1;
				}
				else return 0;
			}
		});
		
		HashMap<String, Suffix> sortedMap = new LinkedHashMap<String, Suffix>();
		
		for(HashMap.Entry<String, Suffix> entry: entries){
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

/**
 * Sort root change by number of occurrences.
 *
 * @param map the map
 * @return the hash map
 */
public HashMap<String,RootChange> sortRootChangeByNumberOfOccurrences(HashMap<String,RootChange> map){
	List<Map.Entry<String,RootChange>> entries = new LinkedList<Map.Entry<String,RootChange>>(map.entrySet());
	
	Collections.sort(entries, new Comparator<Map.Entry<String,RootChange>>(){

		@Override
		public int compare(Entry<String,RootChange> arg0, Entry<String,RootChange> arg1) {
			RootChange sf0 = (RootChange) arg0.getValue();
			RootChange sf1 = (RootChange) arg1.getValue();
			int count1 = sf0.getNumberOfOccurrences();
			int count2 = sf1.getNumberOfOccurrences();
			if(count1 > count2){
				return -1;
			}
			else if(count1 < count2){
				return 1;
			}
			else return 0;
		}
	});
	
	HashMap<String,RootChange> sortedMap = new LinkedHashMap<String,RootChange>();
	
	for(Map.Entry<String,RootChange> entry: entries){
		sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
}

/**
 * Sort forms number of occurrences.
 *
 * @param hmForm the hm form
 * @return the hash map
 */
public HashMap<String, Forms> sortFormsNumberOfOccurrences(HashMap<String, Forms> hmForm){
	List<Map.Entry<String, Forms>> entries = new LinkedList<Map.Entry<String, Forms>>((Collection<? extends Entry<String, Forms>>) hmForm.entrySet());
	
	Collections.sort(entries, new Comparator<Map.Entry<String, Forms>>(){

		@Override
		public int compare(Entry<String, Forms> arg0, Entry<String, Forms> arg1) {
			Forms f0 = (Forms) arg0.getValue();
			Forms f1 = (Forms) arg1.getValue();
			int count1 = f0.getOccurrences();
			int count2 = f1.getOccurrences();
			if(count1 > count2){
				return -1;
			}
			else if(count1 < count2){
				return 1;
			}
			else return 0;
		}
	});
	
	HashMap<String, Forms> sortedMap = new LinkedHashMap<String, Forms>();
	
	for(HashMap.Entry<String, Forms> entry: entries){
		sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
}

}