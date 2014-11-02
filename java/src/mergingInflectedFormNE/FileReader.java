package mergingInflectedFormNE;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class FileReader {

public LinkedList<LinkAnchor> readLinkAnchorFromFile(String file){
	LinkedList<LinkAnchor> llla = new LinkedList<LinkAnchor>(); 
	FileInputStream fstream;
	try {
		fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
		
		String strLine;
		while ((strLine = br.readLine()) != null){
				llla.add(new LinkAnchor(strLine));
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

public LinkedList<NamedEntity> readMergedNE(String file){
	LinkedList<NamedEntity> llNE = new LinkedList<NamedEntity>(); 
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
			if(strLine.equals("============================================================")){
				if(IF.size()!=0){
					llNE.add(new NamedEntity(neName,IF));
					neName = "";
					pomIF = null;
					IF = new LinkedList<InflectedForm>();
				}
			}
			strLine = br.readLine();
			neName = strLine.split("\\t")[1];
			while ((strLine = br.readLine()) != null){
				if(strLine.equals("============================================================")){
					break;
				}
				if(strLine.equals("")){
					llNE.add(new NamedEntity(neName,IF));
					break;
				}
				//System.out.println(strLine);
				String[] pomPole = strLine.split("\\t\\t");
				pomIF = new InflectedForm(pomPole[0]);
				pomIF.setOccurence(Integer.parseInt(pomPole[1]));
				IF.add(pomIF);						
			}
		}	
		llNE.add(new NamedEntity(neName,IF));
		
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
	return llNE;
}
}
