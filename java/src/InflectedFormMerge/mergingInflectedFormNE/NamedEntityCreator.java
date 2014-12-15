package mergingInflectedFormNE;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class NamedEntityCreator.
 */
public class NamedEntityCreator {

	/**
	 * Prepare named entity with count.
	 *
	 * @param hmLinkAnchor the hm link anchor
	 * @return the hash map
	 */
	public HashMap<String, NamedEntity> prepareNamedEntityWithCount(HashMap<String, LinkAnchor> hmLinkAnchor) {
		HashMap<String, NamedEntity> hmNamedEntity = new HashMap<String, NamedEntity>();
		for(Map.Entry<String, LinkAnchor> entry : hmLinkAnchor.entrySet()){
			String key = entry.getKey();
			LinkAnchor la = entry.getValue();
			
			if(hmNamedEntity.containsKey(la.getLink())){
				NamedEntity pomNamedEntity = hmNamedEntity.get(la.getLink());
				pomNamedEntity.addInflectedFormInList(la.getAnchor(),la.getLink(),la.getOccurrences());
			}
			else{
				hmNamedEntity.put(la.getLink(), new NamedEntity(la));
			}
		}		
		return hmNamedEntity;
	}	
	

	/**
	 * Named entity hash map to string with number of occurences.
	 *
	 * @param hmNamedEntity the hm named entity
	 * @return the string
	 */
	public String namedEntityHashMapToStringWithNumberOfOccurences(HashMap<String, NamedEntity> hmNamedEntity){
		int i = 1;
		StringBuilder s = new StringBuilder();
		//String str = "";
		for(Map.Entry<String, NamedEntity> entry : hmNamedEntity.entrySet()){
			String key = entry.getKey();
			NamedEntity ne = entry.getValue();
			//str=str+ "============================================================\n" + ne.getNE();
			s.append("============================================================\n" + i + "	" + ne.getNE());
			//str=str+ i + "  ============================================================";
			for(InflectedForm infe : ne.getListInflectedForms()){
				s.append("\n" + infe.getInflectedForm() + "		" + infe.getOccurrences());
				//s.append("\n" + infe.getInflectedForm());
				//str=str+ "\n" + infe;
			}
			s.append("\n");
			//str=str + "\n";
			i++;
		}
		s.substring(0,s.length()-1);
		//str=str.substring(0, str.length()-1);
		//System.out.println(str);
		//return str;
		return s.toString();
	}
	
	/**
	 * Named entity hash map to string.
	 *
	 * @param hmNamedEntity the hm named entity
	 * @return the string
	 */
	public String namedEntityHashMapToString(HashMap<String, NamedEntity> hmNamedEntity){
		int i = 1;
		StringBuilder s = new StringBuilder();
		for(Map.Entry<String, NamedEntity> entry : hmNamedEntity.entrySet()){
			String key = entry.getKey();
			NamedEntity ne = entry.getValue();
			s.append("_: " + i + " :_\n" + ne.getNE());
			for(InflectedForm infe : ne.getListInflectedForms()){
				s.append("\n" + infe.getInflectedForm() + "	" + infe.getOccurrences());
			}
			s.append("\n");
			i++;
		}
		s.substring(0,s.length()-1);
		return s.toString();
	}

}
