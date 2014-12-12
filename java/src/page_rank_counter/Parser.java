import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
/**
 * Class for parsing input file skwiki latest pagelinks and counting the pagerank for each file.
 * In the end creates a file containing pagerank for each page.
 *
 */

public class Parser {
	private BufferedWriter bw,bufw = null;
	private BufferedReader bufr;
	private BufferedReader brlp = null;
	private ArrayList<Zaznam> zaznam_list = null;
	private ArrayList<Odkaz> odkaz_list = null;
	private String path;
	private double maxKonvergPrem;
	
	Parser()
	{
		maxKonvergPrem = 0.01;
	}
	
	public void setMaxKonvergPrem (double _value)
	{
		maxKonvergPrem = _value;
	}
	public void readFile(String _path) 
	{
		zaznam_list = new ArrayList<Zaznam>();
		odkaz_list = new ArrayList<Odkaz>();
		path = _path;
		p_readFile();
	}
	
	private void p_readFile()
	{
		String sCurrentLine;
		String insert;
		String[] inserts;
		String[] ones;
		int countLines = 0;
		BufferedReader br = null;
		try {
			Charset.forName("UTF-8").newEncoder();
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "\\output-links.txt"),Charset.forName("UTF-8").newEncoder()));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "\\skwiki-latest-pagelinks.sql"),Charset.forName("UTF-8").newDecoder()));
			
			while ((sCurrentLine = br.readLine()) != null) {
				countLines++;
				System.out.println(countLines);
				if(countLines < 80)
				{
					if(sCurrentLine.matches(".*(\\([0-9]+,.*\\))+.*")) {
						insert = sCurrentLine.replaceAll("\\).?,.?\\(", "\\),\\(");
						inserts = insert.split(" ");
						for(int i = 0; i<inserts.length;i++)
						{
							parseInserts(inserts[i]);
						}
					}
				}
				
				
			}
				
			System.out.println("Zaznamy sparsovane, caka sa na id.");
			brlp = new BufferedReader(new InputStreamReader(new FileInputStream(path + "\\output-skwiki-latest-page.txt"),Charset.forName("UTF-8").newDecoder()));				
			findId();
			brlp.close();
			System.out.println("Id pridelene, ideme dalej.");
			br.close();
			countLines = 0;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "\\skwiki-latest-pagelinks.sql"),Charset.forName("UTF-8").newDecoder()));
			while ((sCurrentLine = br.readLine()) != null) {
				countLines++;
				if(countLines<80)
				{
					if(sCurrentLine.matches(".*(\\([0-9]+,.*\\))+.*")) {
						insert = sCurrentLine.replaceAll("\\).?,.?\\(", "\\),\\(");
						inserts = insert.split(" ");
						for(int i = 0; i<inserts.length;i++)
						{
							writeInserts(inserts[i]);
						}
					}
				}
				
				
			}
			bw.close();
			br.close();
			System.out.println("Pageranky nacitane.");
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getPageRank()
	{
		try {
			//countPageRanks();
			System.out.println("Writeing file.");
			writeFile();
			System.out.println("All done.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeFile() throws IOException
	{
		BufferedWriter b = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "\\output-pageRank.txt"),Charset.forName("UTF-8").newEncoder()));
		double pom;
		for(Zaznam z:zaznam_list)
		{
			pom = z.getPageRank()/zaznam_list.size();
			b.append(z.getId() + ";" + z.getMeno() + ";" + String.format("%4.3f", pom) + "\n");
			
		}
	}
	
	private void parseInserts(String insert) throws IOException
	{

		if(insert.matches(".*,.*"))
		{
			String[] ones = insert.split("\\),\\(");
			ones[0] = ones[0].replaceAll("[\\(\\)']", "");
			ones[ones.length-1] = ones[ones.length-1].replaceAll("[\\(\\)']", "");
			for(int j = 0; j < ones.length;j++){
				//ones[j] = ones[j].replaceAll("[']", "");
				splitInsert(ones[j]);	
			}
			
		}
	}
	
	private void countPageRanks() throws IOException
	{
		String sLine;
		String[] parts;
		Odkaz o = null;
		Zaznam zaz = null;
		double rank = 1; //na porovnanie zmeny pageranku
		double rankChange = 1;
		PageRank pr = new PageRank();
		String file1 = path + "\\output-links-help.txt";
		String file2 = path + "\\output-links.txt";
		String pom = "";
		while(rankChange > maxKonvergPrem)
		{
				rankChange = 0;
				bufw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1),Charset.forName("UTF-8").newEncoder()));
				bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file2),Charset.forName("UTF-8").newDecoder()));
				while ((sLine = bufr.readLine()) != null) {
					sLine = sLine.replaceAll(",","\\.");
					parts = sLine.split(";");
					zaz = najdiZaznam(parts[3]);
					zaz.setPageRankCount(pr.countNext(Double.parseDouble(parts[2]), Double.parseDouble(parts[1]), zaz.getPageRankCount()));
				}
				System.out.println("Page Rank spocitany.");
				for(Zaznam z:zaznam_list)
				{
					
					rank = z.getPageRank();
					
					z.setPageRank(pr.getPageRank(z.getPageRankCount()));
					rankChange += Math.abs(rank - z.getPageRank());
					if((o = findOdkaz(z.getId())) != null)
					{
						o.setPageRank(z.getPageRank());
					}
					z.setPageRankCount(0);
					
				}
				bufr.close();
				bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file2),Charset.forName("UTF-8").newDecoder()));
				System.out.println("Odkazy prepojene");
				while ((sLine = bufr.readLine()) != null) {
					parts = sLine.split(";");
					o = findOdkaz(parts[0]);
					zaz = najdiZaznam(parts[3]);
					bufw.append(parts[0] + ";" + o.getCount() + ";" + String.format("%4.3f", o.getPageRank()) + ";" + zaz.getId() + ";" + String.format("%4.3f", zaz.getPageRank()) + "\n");
					
				}
				bufw.close();
				bufr.close();
				pom = file1;
				file1 = file2;
				file2 = pom;
				rankChange = rankChange / zaznam_list.size();
				System.out.println(rankChange);
				
		}
	}
	private Zaznam najdiZaznam(String id)
	{
		for(Zaznam z : zaznam_list)
		{
			if(z.getId().equalsIgnoreCase(id))
			{
				return z;
			}
		}
		return null;
	}
	private void writeInserts(String insert) throws IOException
	{

		if(insert.matches(".*,.*"))
		{
			String[] ones = insert.split("\\),\\(");
			ones[0] = ones[0].replaceAll("[\\(\\)']", "");
			ones[ones.length-1] = ones[ones.length-1].replaceAll("[\\(\\)']", "");
			for(int j = 0; j < ones.length;j++){
				//ones[j] = ones[j].replaceAll("[']", "");
				writeOnes(ones[j]);	
			}
			
		}
	}
	private void writeOnes(String one) throws IOException
	{
		String[] chunks = one.split(",");
		Odkaz od = findOdkaz(chunks[0]);
		Zaznam zaz = findZaznam(chunks[2]);
		
		bw.append(chunks[0] + ";" + od.getCount() + ";" + od.getPageRank() + ";" + zaz.getId() + ";" + zaz.getPageRank() + "\n");
	}
	
	
	
	
	private void splitInsert(String insert) throws IOException
	{
		String[] chunks = insert.split(",");
		Odkaz o = null;
		if((o = findOdkaz(chunks[0])) == null)
		{
			Odkaz odk = new Odkaz(chunks[0]);
			odkaz_list.add(odk);
			
		}
		else
		{
			o.setCount(1);
		}
		
		if(!b_findZaznam(chunks[2]))
		{
			Zaznam z = new Zaznam(chunks[2]);
			zaznam_list.add(z);
		}
		
	}
	
	private Odkaz findOdkaz(String odkId)
	{
		for(Odkaz o : odkaz_list)
		{
			if(o.getId().equalsIgnoreCase(odkId))
			{
				return o;
			}
		}
		
		return null;
	}
	
	private boolean b_findZaznam(String zaz)
	{
		for(Zaznam z : zaznam_list)
		{
			if(z.getMeno().equalsIgnoreCase(zaz))
			{
				return true;
			}
		}
		return false;
	}
	
	private void findId() throws IOException
	{
		String newLine;
		String[] chunks;
		Zaznam zaz = null;
		while((newLine = brlp.readLine()) != null)
		{
			chunks = newLine.split(";");
			if((zaz = findZaznam(chunks[2])) != null)
			{
				zaz.setId(chunks[0]);
			}
		}
	}
	
	private Zaznam findZaznam(String name)
	{
		for(Zaznam z : zaznam_list)
		{
			if(z.getMeno().equalsIgnoreCase(name))
			{
				return z;
			}
		}
		return null;
	}
}
