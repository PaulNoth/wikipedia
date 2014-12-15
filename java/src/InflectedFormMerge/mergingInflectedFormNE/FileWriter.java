package mergingInflectedFormNE;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import testAndTools.SortHashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class FileWriter.
 */
public class FileWriter {

	/**
	 * Write root change to file.
	 *
	 * @param outputFilePath the output file path
	 * @param hmRootChange the hm root change
	 * @param addInfo the add info
	 */
	public void writeRootChangeToFile(String outputFilePath, HashMap<String, RootChange> hmRootChange, boolean addInfo){
		SortHashMap sHM = new SortHashMap();
		hmRootChange = sHM.sortRootChangeByNumberOfOccurrences(hmRootChange);
		
		BufferedWriter out;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath),"UTF-8"));
			
			for(Map.Entry<String, RootChange> entry : hmRootChange.entrySet()){
				RootChange rc = entry.getValue();
				
				if(addInfo){
					out.write(rc.getRootBeforeChange() + "	" + rc.getRootChange() + "	" + rc.getNumberOfOccurrences());
					out.newLine();
					
					HashMap<String, Forms> pomForm = rc.getHashMapForms();
					pomForm = sHM.sortFormsNumberOfOccurrences(pomForm);
					
					for(Map.Entry<String, Forms> e : pomForm.entrySet()){
						Forms f = e.getValue();
						out.write("	" + f.getForm1() + "	" + f.getForm2() + "	" + f.getOccurrences());
						out.newLine();
					}				
				}
				else{
					out.write(rc.getRootChange() + "	" + rc.getRootBeforeChange() + "	" + rc.getNumberOfOccurrences());
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			System.out.println("Error: " + "could not write to file" + "  " + outputFilePath);
			e.printStackTrace();
		}
	}	
	
/**
 * Write suffixes to file.
 *
 * @param outputFilePath the output file path
 * @param hmSuffix the hm suffix
 * @param addInfo the add info
 */
public void writeSuffixesToFile(String outputFilePath, HashMap<String, Suffix> hmSuffix, boolean addInfo){
	SortHashMap sHM = new SortHashMap();
	hmSuffix = sHM.sortSuffixesByNumberOfOccurrences(hmSuffix);
	BufferedWriter out;
	try {
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath),"UTF-8"));
		
		for(Map.Entry<String, Suffix> entry : hmSuffix.entrySet()){
			Suffix suf = entry.getValue();
			
			if(addInfo){
				out.write(suf.getSuffix() + "	" + suf.getNumberOfOccurrences());
				out.newLine();
				
				HashMap<String, Forms> pomForm = suf.getStringsWithSuffix();
				pomForm = sHM.sortFormsNumberOfOccurrences(pomForm);
				
				for(Map.Entry<String, Forms> e : pomForm.entrySet()){
					Forms f = e.getValue();
					out.write("	" + f.getForm1() + "	" + f.getForm2() + "	" + f.getOccurrences());
					out.newLine();
				}				
			}
			else{
				out.write(suf.getSuffix() + "	" + suf.getNumberOfOccurrences());
			}
			out.newLine();
		}
		out.close();
	} catch (IOException e) {
		System.out.println("Error: " + "could not write to file" + "  " + outputFilePath);
		e.printStackTrace();
	}
}
	
/**
 * Write links to file.
 *
 * @param outputFilePath the output file path
 * @param hmLinkAnchor the hm link anchor
 * @param withOccurrenceCount the with occurrence count
 */
public void writeLinksToFile(String outputFilePath, HashMap<String, LinkAnchor> hmLinkAnchor, boolean withOccurrenceCount){
	BufferedWriter out;
	try {
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath),"UTF-8"));
		
		for(Map.Entry<String, LinkAnchor> entry : hmLinkAnchor.entrySet()){
			String key = entry.getKey();
			LinkAnchor la = entry.getValue();
			
			if(la.getAnchor().length()>0){
				if(withOccurrenceCount){
					out.write("[["+la.getLink().trim()+"|"+la.getAnchor().trim()+"]]	" + la.getOccurrences());
				}
				else{
					out.write("[["+la.getLink().trim()+"|"+la.getAnchor().trim()+"]]");
				}
				out.newLine();
			}
			else{
				if(withOccurrenceCount){
					out.write("[["+la.getLink().trim()+"]]	" + la.getOccurrences());
				}
				else{
					out.write("[["+la.getLink().trim()+"]]");
				}
				out.newLine();
			}	
		}
		out.close();
	} catch (IOException e) {
		System.out.println("Error: " + "could not write to file" + "  " + outputFilePath);
		e.printStackTrace();
	}
}

/**
 * Write named entity to file.
 *
 * @param outputFilePath the output file path
 * @param hmNamedEntity the hm named entity
 */
public void writeNamedEntityToFile(String outputFilePath, HashMap<String, NamedEntity> hmNamedEntity){
	BufferedWriter out;
	try {
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath),"UTF-8"));
		
		int i = 1;
		for(Map.Entry<String, NamedEntity> entry : hmNamedEntity.entrySet()){
			String key = entry.getKey();
			NamedEntity ne = entry.getValue();
			
			out.write("_: " + Integer.toString(i) + " :_");
			out.newLine();
			out.write(ne.getNE());
			out.newLine();
			
			for(InflectedForm infe : ne.getListInflectedForms()){
				out.write(infe.getInflectedForm() + "	" + infe.getOccurrences());
				out.newLine();
			}
			i++;
		}
		out.close();
	} catch (IOException e) {
		System.out.println("Error: " + "could not write to file" + "  " + outputFilePath);
		e.printStackTrace();
	}
}

}
