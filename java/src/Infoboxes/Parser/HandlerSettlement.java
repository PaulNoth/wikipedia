package Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerSettlement  extends DefaultHandler {
	
	//zoznam najdenych infoboxov typu settlement
	private List<Infobox_settlement> infobox_settlementList = null;
	//aktualne spracovavany infobox
	private Infobox_settlement infobox_settlement = null;
	private StringBuffer sb;
	//pocitadlo
	int counter = 0;
	
	public List<Infobox_settlement> getInfoboxList() {
        return infobox_settlementList;
    }

	boolean bTitle = false;
	Help pomoc = new Help();
 
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
    	sb = new StringBuffer();
    
        if (qName.equalsIgnoreCase("Page")) {                
        	infobox_settlement = new Infobox_settlement();
            if (infobox_settlementList == null) {
            	infobox_settlementList = new ArrayList<>();
            }
            //vyparsovanie atributu text
        } else if (qName.equalsIgnoreCase("text")) {
        	bTitle = true;
        } 
    }

    //metoda na oparsovanie infoboxu, v atribute vysledok je ulozeny text na spracovanie
    public boolean oparsujSettlement(boolean flag, String vysledok){
    	flag = false;
    	
    	//postupne vyberanie jednotlivych atributov z textu
    	String vystup;
    	//zoznam regexov ktory sa pouzije na ziskanie vacsiny atributov
    	List<String> list_regexov = new ArrayList<String>();
    	List<String> list_atributov = new ArrayList<String>();
    	
    	list_regexov.add("\\| ?official_name ?= [^|]+");
    	list_regexov.add("\\| ?nickname ?= [^|]+");
    	list_regexov.add("\\| ?map_caption ?= [^|]+");
    	list_regexov.add("\\| ?coordinates_region ?= [^|]+");
    	list_regexov.add("\\| ?leader_title ?= [^|]+");
    	list_regexov.add("\\| ?unit_pref ?= [^|]+");
    	list_regexov.add("\\| ?area_total_km2 ?= [^|]+");
    	list_regexov.add("\\| ?area_land_km2 ?= [^|]+");
    	list_regexov.add("\\| ?population_density_km2 ?= [^|]+");
    	list_regexov.add("\\| ?website ?= [^|}{]+");
    	
    	list_atributov.add("official_name");
    	list_atributov.add("nickname");
    	list_atributov.add("map_caption");
    	list_atributov.add("coordinates_region");
    	list_atributov.add("leader_title");
    	list_atributov.add("unit_pref");
    	list_atributov.add("area_total_km2");
    	list_atributov.add("area_land_km2");
    	list_atributov.add("population_density_km2");
    	list_atributov.add("website");
    	
    	for(int i=0;i<list_regexov.size();i++){
    		vystup = pomoc.PouziRegex(list_regexov.get(i), vysledok);  	
        	if (vystup != null){
        		vystup = pomoc.ocisti_retazec(vystup, list_atributov.get(i));
        		if (list_atributov.get(i).contains("website")){
        			vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-/]","");
        		}
        		else {
        			vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-]","");
        		}
        		vystup = pomoc.posledna_medzera(vystup);
        		if (list_atributov.get(i).contains("official_name")){
        			infobox_settlement.setOfficial_name(vystup);
        		}
        		else if (list_atributov.get(i).contains("nickname")){
        			infobox_settlement.setNickname(vystup);
        		}
        		else if (list_atributov.get(i).contains("map_caption")){
        			infobox_settlement.setMap_caption(vystup);
        		}
        		else if (list_atributov.get(i).contains("coordinates_region")){
        			infobox_settlement.setCoordinates_region(vystup);
        		}
        		else if (list_atributov.get(i).contains("leader_title")){
        			infobox_settlement.setLeader_title(vystup);
        		}
        		else if (list_atributov.get(i).contains("unit_pref")){
        			infobox_settlement.setUnit_pref(vystup);
        		}
        		else if (list_atributov.get(i).contains("area_total_km2")){
        			infobox_settlement.setArea_total_km2(vystup);
        		}
        		else if (list_atributov.get(i).contains("area_land_km2")){
        			infobox_settlement.setArea_land_km2(vystup);
        		}
        		else if (list_atributov.get(i).contains("population_density_km2")){
        			infobox_settlement.setPopulation_density_km2(vystup);
        		}
        		else if (list_atributov.get(i).contains("website")){
        			infobox_settlement.setWebsite(vystup);
        		}
        	    flag = true;
        	}
    	}
 
    	vystup = pomoc.PouziRegex("\\| ?population_total ?= [A-Za-z0-9 _ =*.:?!()+-<>\\]\\[#@\\{|'`$%^&;<>,ֹציז]+", vysledok);  	
    	if (vystup != null){
    		
    		if(vystup.contains("population_density_km2") ){
    			String[] parts = vystup.split("population_density_km2");
    			vystup = parts[0]; 
    		}
    		
    		if(vystup.contains("population_as_of") ){
    			String[] parts = vystup.split("population_as_of");
    			vystup = parts[0]; 
    		}
    		
    		vystup = pomoc.ocisti_retazec(vystup, "population_total");
    		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-]","");
    		vystup = pomoc.posledna_medzera(vystup);
    	    infobox_settlement.setPopulation_total(vystup);
    	    flag = true;
    	}

    	vystup = pomoc.PouziRegex("\\| ?timezone ?= [A-Za-z0-9 _ =*.:?!()+-<>\\[#@\\{}|'` $%^&;<>,ֹציז]+", vysledok);  	
    	if (vystup != null){
    		if(vystup.contains("utc_offset") ){
    			String[] parts = vystup.split("utc_offset");
    			vystup = parts[0]; 
    		}
    		if(vystup.contains("timezone_DST") ){
    			String[] parts = vystup.split("timezone_DST");
    			vystup = parts[0]; 
    		}
    		
    		if(vystup.contains("utc_offset1") ){
    			String[] parts = vystup.split("utc_offset1");
    			vystup = parts[0]; 
    		}
    		
    		vystup = pomoc.ocisti_retazec(vystup, "timezone");
    		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-|]","");
    		vystup = vystup.replaceAll("\\[","");
    		vystup = vystup.replaceAll("\\]","");
    		vystup = pomoc.posledna_medzera(vystup);
    		String a = vystup.substring(0, 1);
    		if(a.contains("|")){
    			infobox_settlement.setTimezone(null);
    		}
    			
    	    infobox_settlement.setTimezone(vystup);
    	    flag = true;
    	}
    	
    	vystup = pomoc.PouziRegex("\\| ?postal_code ?= [^|]+", vysledok);  	
    	if (vystup != null){
    		vystup = pomoc.ocisti_retazec(vystup, "postal_code");
    		
    		vystup = vystup.replaceAll("[^0-9.:,?! +-\\[\\];\\/*]","");
    		String[] rozdelovac = {",","-"};
    		if (vystup != null){
    		String[] pole =  pomoc.rozdel_do_pola(vystup,rozdelovac);
    		String a;		
    		for(int i=0;i<pole.length;i++){
    			pole[i]  = pomoc.posledna_medzera(pole[i]);
    			infobox_settlement.setPostal_code(pole);
    		}
    		}
      
    	    flag = true;
    	}
    	
    	counter++;
    	return flag;
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	boolean flag_settlement = false;
    	
    	///ak najdem koniec
        if (bTitle) {
        	
        	//do premennej vysledok si ulozim vyparsovany infobox
        	String vysledok = sb.toString();
        	 //zarovnanie do jedneho riadku
        	vysledok = vysledok.replaceAll("(\r\n|\n)", " ");
        	//odstranenie nepotrebnych medzier
        	vysledok = vysledok.trim().replaceAll(" +", " "); 
        	//vybratie samotneho infoboxu
        	String text = pomoc.PouziRegex("\\{\\{Infobox settlement \\s*(.*)", vysledok);
        	
        	//spustim vyparsovanie jednotlivych atributov z infoboxu
        	if (text !=null){
        		flag_settlement  = oparsujSettlement(flag_settlement, text);
        	}
        	//ak som nieco vyparsoval...
        	if (flag_settlement == true){
        		//infobox settlement musi obsahovat official name alebo population total....
        		if ( infobox_settlement.getOfficial_name()!= null ||  infobox_settlement.getPopulation_total() != null){
        			String psc = Arrays.toString(infobox_settlement.getPostal_code());
        			//ak obsahuje tak ho pridam do listu
        			infobox_settlementList.add(infobox_settlement);
            	  }
        		System.out.println(counter);
        	}	
            bTitle = false;
            flag_settlement = false;
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
    	// spracovavanie vstupu
         if (sb!=null && bTitle) {
             for (int i=start; i<start+length; i++) {
            	 sb.append(ch[i]);
             }
             
         }
         
    }
}
