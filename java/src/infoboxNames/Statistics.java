package infoboxNames;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Statistics {

	String actualArticle;
	int numberOfArticles;
	HashMap<String, Long> mapNameCount = new HashMap<String, Long>();
	HashMap<String, Long> mapGenreCount = new HashMap<String, Long>();
	HashMap<String, Long> mapPopulation = new HashMap<String, Long>();
	
	
	public Statistics(){
		this.actualArticle = "";
		this.numberOfArticles = 0;
	}
	
	public void ProcessLine(String line){
		
		//System.out.println(line);
		//split line to needed information
		String[] nameTokens = line.split("\t");
		String[] propertyTokens = nameTokens[1].split(" = ");
		long nameCount;
		//System.out.println(propertyTokens[0] + "\n" + propertyTokens[1]);
		
		//if new name, increase number of articles
		if(nameTokens[0].compareTo(actualArticle)!=0){
			actualArticle = nameTokens[0];
			numberOfArticles++;
		}
		
		//if line contains infobox name, we put it into hashmap for names and increase count
		if(propertyTokens[0].compareTo("infobox")==0){
			if(mapNameCount.containsKey(propertyTokens[1])){
				nameCount = mapNameCount.get(propertyTokens[1]);
				mapNameCount.put(propertyTokens[1], ++nameCount);
			}
			else
			{
				mapNameCount.put(propertyTokens[1], (long) 1);
			}
		}
		
		//if line contains genre, we put it into hashmap for genres and increase count
		if(propertyTokens[0].compareTo("genre")==0){
			if(mapGenreCount.containsKey(propertyTokens[1])){
				nameCount = mapGenreCount.get(propertyTokens[1]);
				mapGenreCount.put(propertyTokens[1], ++nameCount);
			}
			else
			{
				mapGenreCount.put(propertyTokens[1], (long) 1);
			}
		}

		//if line contains population_estimate, we put it into hashmap for population and increase count
		if(propertyTokens[0].compareTo("population_estimate")==0){
			if(mapGenreCount.containsKey(actualArticle)){
				//nameCount = mapGenreCount.get(propertyTokens[1]);
				//mapGenreCount.put(propertyTokens[1], ++nameCount);
			}
			else
			{
				if(propertyTokens.length == 2){
					mapPopulation.put(actualArticle, Long.parseLong(propertyTokens[1].replaceAll("\\.|,|\\s*", "").replaceAll("million", "000000")) );
				}
			}
		}
	
	}
	
	//sorting hashmaps and writing statistics to files
	public void WriteStatistics() throws IOException{
		

		StringBuilder sbNameCount = new StringBuilder();
		StringBuilder sbGenreCount = new StringBuilder();
		StringBuilder sbPopulation = new StringBuilder();
		
		//writing number of articles with infobox
		System.out.println("\nNumber of articles with infobox: " + numberOfArticles + "\n");
		//System.out.println(mapNamesCount);
		
		//writing infobox names count
		//System.out.println("Sorting infobox name hashmap...");
		mapNameCount = sortByValues(mapNameCount);
		
		Iterator<String> keySetIterator = mapNameCount.keySet().iterator();

		while(keySetIterator.hasNext()){
		  String key = keySetIterator.next();
		  //System.out.println(mapNamesCount.get(key) +"\t" + key);
		  sbNameCount.append(mapNameCount.get(key) + "\t" + key + "\n");			
		}
		
		File countFileName = new File("C:/Temp/infoboxNameCount.txt");
		FileWriter fwNameCount = new FileWriter(countFileName);
		fwNameCount.append(sbNameCount);
		fwNameCount.close();
		
		//writing infobox genre count
		//System.out.println("Sorting infobox genre hashmap...");
		mapGenreCount = sortByValues(mapGenreCount);
		
		Iterator<String> keySetIterator2 = mapGenreCount.keySet().iterator();

		while(keySetIterator2.hasNext()){
		  String key = keySetIterator2.next();
		  //System.out.println(mapGenreCount.get(key) +"\t" + key);
		  sbGenreCount.append(mapGenreCount.get(key) + "\t" + key + "\n");			
		}
		
		File countFileGenre = new File("C:/Temp/infoboxGenreCount.txt");
		FileWriter fwGenreCount = new FileWriter(countFileGenre);
		fwGenreCount.append(sbGenreCount);
		fwGenreCount.close();
		
		//writing infobox population count
		//System.out.println("Sorting infobox population hashmap...");
		mapPopulation = sortByValues(mapPopulation);
		
		Iterator<String> keySetIterator3 = mapPopulation.keySet().iterator();

		while(keySetIterator3.hasNext()){
		  String key = keySetIterator3.next();
		  //System.out.println(mapPopulation.get(key) +"\t" + key);
		  sbPopulation.append(mapPopulation.get(key) + "\t" + key + "\n");			
		}
		
		File countPopulation = new File("C:/Temp/infoboxPopulation.txt");
		FileWriter fwPopulation = new FileWriter(countPopulation);
		fwPopulation.append(sbPopulation);
		fwPopulation.close();
		
		//System.out.println(mapPopulation);
	}
	
	//sorting hashmap by values
	private static HashMap sortByValues(HashMap map) { 
	      List list = new LinkedList(map.entrySet());
	      //comparator
	      Collections.sort(list, new Comparator() {
	           public int compare(Object o2, Object o1) {
	              return ((Comparable) ((Map.Entry) (o1)).getValue())
	                 .compareTo(((Map.Entry) (o2)).getValue());
	           }
	      });

	      //copy sorted list into hashmap using linkedhashmap to preserve the insertion order
	      HashMap sortedHashMap = new LinkedHashMap();
	      for (Iterator it = list.iterator(); it.hasNext();) {
	             Map.Entry entry = (Map.Entry) it.next();
	             sortedHashMap.put(entry.getKey(), entry.getValue());
	      } 
	      return sortedHashMap;
	 }
		
}
