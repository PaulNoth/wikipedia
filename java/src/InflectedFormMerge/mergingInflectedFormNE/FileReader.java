package mergingInflectedFormNE;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class FileReader.
 */
public class FileReader {

	/**
	 * Read root change from file.
	 *
	 * @param file the file
	 * @return the hash map
	 */
	public HashMap<String, RootChange> readRootChangeFromFile(String file){
		HashMap<String, RootChange> hmRootChange = new HashMap<String, RootChange>(); 
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
			
			String strLine;
			
			String rootChange = "";
			String rootBeforeChange = "";
			RootChange rc = null;
			int rcOccurrence = 0;
			
			while ((strLine = br.readLine()) != null){
				if(strLine.matches("\t\\p{L}+\t\\p{L}+\t[0-9]+")){		//matches strings that contains root changes
					if(rc==null){
						System.out.println("Error: wrong file format");
					}
					else{
						//System.out.println(strLine);
						String[] sPole = strLine.split("\t");
						rc.addOccurrence(sPole[1], sPole[2],Integer.parseInt(sPole[3]));
						//System.out.println(sPole[1] + "	" + sPole[2] + "	" + Integer.parseInt(sPole[3]));
					}
				}
				else if(strLine.matches("^\\p{L}+\t\\p{L}+\t[0-9]+")){		//matches root change occurrences and occurrences	
					if(rc != null){
						hmRootChange.put(rootChange+"_"+rootBeforeChange, rc);
						rc = null;						
					}
					String[] sPole = strLine.split("\t");
					rootChange = sPole[0];
					rootBeforeChange = sPole[1];
					//sufOccurrence = Integer.parseInt(sPole[1]);
					rc = new RootChange(rootBeforeChange, rootChange);
				}
			}
			br.close();
			in.close();
			fstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hmRootChange;
	}
	
	/**
	 * Read suffix from file.
	 *
	 * @param file the file
	 * @return the hash map
	 */
	public HashMap<String, Suffix> readSuffixFromFile(String file){
		HashMap<String, Suffix> hmSuffix = new HashMap<String, Suffix>(); 
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
			
			String strLine;

			String suffix = "";
			Suffix suf = null;
			int sufOccurrence = 0;
			
			while ((strLine = br.readLine()) != null){
									
				if(strLine.matches("\t\\p{L}+\t\\p{L}+\t[0-9]+")){	//matches strings that contains suffixes	
					if(suf==null){
						System.out.println("Error: wrong file format - suffix");
					}
					else{
						String[] sPole = strLine.split("\t");
						//System.out.println(strLine);
						suf.addOccurrence(Integer.parseInt(sPole[3]), sPole[1], sPole[2]);
					}
				}
				else if(strLine.matches("^\\p{L}+\t[0-9]+")){			//matches suffix and occurrences
					if(suf != null){
						hmSuffix.put(suffix, suf);
						suf = null;						
					}
					String[] sPole = strLine.split("\t");
					suffix = sPole[0];
					//sufOccurrence = Integer.parseInt(sPole[1]);
					suf = new Suffix(suffix);
				}
			}
			br.close();
			in.close();
			fstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hmSuffix;
	}	
	
	/**
	 * Read named entity.
	 *
	 * @param file the file
	 * @return the hash map
	 */
	public HashMap<String,NamedEntity> readNamedEntity(String file){
		HashMap<String, NamedEntity> hmNE = new HashMap<String,NamedEntity>(); 
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
			
			String strLine;
			String neName = "";
			InflectedForm pomIF = null;
			LinkedList<InflectedForm> IF = new LinkedList<InflectedForm>();
			
			strLine = br.readLine();
			while (strLine != null){
				if(strLine.matches("^_: [0-9]+ :_$")){
					if(IF.size()!=0){
						hmNE.put(neName, new NamedEntity(neName,IF));
						neName = "";
						pomIF = null;
						IF = new LinkedList<InflectedForm>();
					}
				}

				strLine = br.readLine();
				neName = strLine;
				while ((strLine = br.readLine()) != null){
					if(strLine.matches("^_: [0-9]+ :_$")){
						break;
					}
					if(strLine.equals("")){
						hmNE.put(neName, new NamedEntity(neName,IF));
						break;
					}
					if(strLine.matches(".+\t[0-9]+$")){
						String[] sSplit = strLine.split("\t");
						
						if(sSplit.length<2){
							System.out.println(sSplit.length);
							System.out.println(strLine);
						}
						
						IF.add(new InflectedForm(sSplit[0].trim(), Integer.parseInt(sSplit[1])));
					}

				}
			}	
			hmNE.put(neName, new NamedEntity(neName,IF));
			
			br.close();
			in.close();
			fstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hmNE;
	}
	
	
	/**
	 * Read link anchor from file.
	 *
	 * @param file the file
	 * @param withOccurrenceCount the with occurrence count
	 * @return the linked list
	 */
	public LinkedList<LinkAnchor> readLinkAnchorFromFile(String file, boolean withOccurrenceCount){
		LinkedList<LinkAnchor> llla = new LinkedList<LinkAnchor>(); 
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
			
			String strLine;
			while ((strLine = br.readLine()) != null){
				if(withOccurrenceCount){
					String[] sSplit = strLine.split("\t");
					LinkAnchor pomLA = new LinkAnchor(sSplit[0]);
					pomLA.setOccurrence(Integer.parseInt(sSplit[1]));
					llla.add(pomLA);					
				}
				else{
					llla.add(new LinkAnchor(strLine));
				}
				
			}		
			br.close();
			in.close();
			fstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return llla;
	}	
	
	
	/**
	 * Read link anchor from file.
	 *
	 * @param file the file
	 * @return the hash map
	 */
	public HashMap<String, LinkAnchor> readLinkAnchorFromFile(String file){
		HashMap<String, LinkAnchor> hmLinkAnchor = new HashMap<String, LinkAnchor>(); 
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
			
			String strLine;
			while ((strLine = br.readLine()) != null){
					//strLine = strLine.replaceAll("\\|\t", "|").replaceAll("\\[\\[\t", "[[");		//this should not be here - problem is probably in named LinkAnchor
					//strLine = strLine.replaceAll("\\]\\]", "]]]]");
					//String[] sSplit = strLine.split("]]\t");
					String[] sSplit = strLine.split("\t");
					LinkAnchor pomLA = new LinkAnchor(sSplit[0].trim());
					
					if(sSplit.length>2){
						System.out.println(sSplit.length);
						System.out.println(strLine);
					}

					pomLA.setOccurrence(Integer.parseInt(sSplit[1]));
					hmLinkAnchor.put(pomLA.getLink()+pomLA.getAnchor(), pomLA);					
			}		
			br.close();
			in.close();
			fstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hmLinkAnchor;
	}	
	
}
