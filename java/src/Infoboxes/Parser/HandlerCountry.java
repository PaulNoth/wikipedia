package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerCountry  extends DefaultHandler {
	
	//zoznam najdenych infoboxov typu country
	private List<Infobox_country> infoboxList = null;
	//aktualne spracovavany infobox
	private Infobox_country infobox = null;
	private StringBuffer sb;
	//pocitadlo
	int counter = 0;
	public List<Infobox_country> getInfoboxList() {
        return infoboxList;
    }
	Help pomoc = new Help();
	boolean bTitle = false;

    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
    	sb = new StringBuffer();
    	//ak najdem page vytvorim novy infobox
        if (qName.equalsIgnoreCase("Page")) {                
            infobox = new Infobox_country();
            if (infoboxList == null) {
            	infoboxList = new ArrayList<>();
            }
          //vyparsovanie atributu text
        }  else if (qName.equalsIgnoreCase("text")) {
        	bTitle = true;
        } 
    }
    
  //metoda na oparsovanie infoboxu, v atribute vysledok je ulozeny text na spracovanie
    public boolean oparsujCountry(boolean flag, String vysledok){
    	String vystup;
    	//zoznam regexov ktory sa pouzije na ziskanie vacsiny atributov
    	List<String> list_regexov = new ArrayList<String>();
    	List<String> list_atributov = new ArrayList<String>();
    	
    	list_regexov.add("\\| ?conventional_long_name ?= [^|]+");
    	list_regexov.add("\\| ?common_name ?= [^|]+");
    	list_regexov.add("\\| ?image_flag ?= [^|]+");
    	list_regexov.add("\\| ?image_coat ?= [^|]+");
    	list_regexov.add("\\| ?area_km2 ?= [^|]+");
    	list_regexov.add("\\| ?area_sq_mi ?= [^|]+");
    	list_regexov.add("\\| ?population_estimate ?= [^|]+");
    	list_regexov.add("\\| ?population_estimate_rank ?= [^|]+");
    	list_regexov.add("\\| ?currency_code ?= [^|]+");
    	
    	list_atributov.add("conventional_long_name");
    	list_atributov.add("common_name");
    	list_atributov.add("image_flag");
    	list_atributov.add("image_coat");
    	list_atributov.add("area_km2");
    	list_atributov.add("area_sq_mi");
    	list_atributov.add("population_estimate");
    	list_atributov.add("population_estimate_rank");
    	list_atributov.add("currency_code");
    	
    	for(int i=0;i<list_regexov.size();i++){
    		vystup = pomoc.PouziRegex(list_regexov.get(i), vysledok);  	
        	if (vystup != null){
        		vystup = pomoc.ocisti_retazec(vystup, list_atributov.get(i));
        		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-]","");
        		vystup = vystup.replaceAll("  "," ");
        		vystup = pomoc.posledna_medzera(vystup);
        		if(list_atributov.get(i).contains("conventional_long_name")){
        			infobox.setTitle(vystup);
        		}
        		else if (list_atributov.get(i).contains("common_name")){
        			infobox.setCommon_name(vystup);
        		}
        		else if (list_atributov.get(i).contains("image_flag")){
        			infobox.setImage_flag(vystup);
        		}
        		else if (list_atributov.get(i).contains("image_coat")){
        			infobox.setImage_coat(vystup);
        		}
        		else if (list_atributov.get(i).contains("area_km2")){
        			infobox.setArea_km2(vystup);
        		}
        		else if (list_atributov.get(i).contains("area_sq_mi")){
        			infobox.setArea_sq_mi(vystup);
        		}
        		else if (list_atributov.get(i).contains("population_estimate")){
        			infobox.setPopulation_estimate(vystup);
        		}
        		else if (list_atributov.get(i).contains("population_estimate_rank")){
        			infobox.setPopulation_estimate_rank(vystup);
        		}
        		else if (list_atributov.get(i).contains("currency_code")){
        			infobox.setCurrency_code(vystup);
        		}
        		flag = true;
        	}
    	}
    	//postupne vyberanie jednotlivych atributov z textu
    	vystup = pomoc.PouziRegex("\\| ?capital ?= [^|]+", vysledok);  	
    	if (vystup != null){
    		if(vystup.contains("latd") ){
    			String[] parts = vystup.split("latd");
    			vystup = parts[0]; 
    		}
    		
    		vystup =pomoc.ocisti_retazec(vystup, "capital");
    		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-]","");
    		vystup = vystup.replaceAll("  "," ");
    		vystup = pomoc.posledna_medzera(vystup);
    	    infobox.setCapital(vystup);
     	    flag = true;
    	}
    	
    	vystup = pomoc.PouziRegex("\\| ?official_religion ?= [A-Za-z0-9 _ =*.:?!()+-<>\\]\\[#@\\{}'`$%^&;<>,ֹציז]+", vysledok);  	
    	if (vystup != null){
    		vystup =pomoc.ocisti_retazec(vystup, "official_religion");
    		String[] rozdelovac = {"!!"};
    		if (vystup != null){
    		String upraveny = pomoc.priprav_pole(vystup);
    		String[] pole =  pomoc.rozdel_do_pola(upraveny,rozdelovac);
    		String a;		
    		for(int i=0;i<pole.length;i++){
    			pole[i]  = pomoc.posledna_medzera(pole[i]);
    			infobox.setOfficial_religion(pole);
    		}
    		}
    	   
     	    flag = true;
    	}
    	
    	vystup = pomoc.PouziRegex("\\| ?official_languages ?= [A-Za-z0-9 _ =*.:?!()+-<>\\}\\[#@\\{|'`$%^&;<>,ֹציז]+", vysledok);  	
    	if (vystup != null){
    		vystup =pomoc.ocisti_retazec(vystup, "official_languages");
    		String[] rozdelovac = {"!!"};
    		if (vystup != null){
    		String upraveny = pomoc.priprav_pole(vystup);
    		String[] pole =  pomoc.rozdel_do_pola(upraveny,rozdelovac);
    		String a;		
    		for(int i=0;i<pole.length;i++){
    			pole[i]  = pomoc.posledna_medzera(pole[i]);
    			infobox.setOfficial_languages(pole);
    		}
    		}
    	    
     	    flag = true;
    	}
    	
    	vystup = pomoc.PouziRegex("\\| ?government_type ?= [A-Za-z0-9 _ =*.:?!()+-<>\\]\\[ #@\\{}'`$%^&;<>,ֹציז|]+", vysledok);  	
    	if (vystup != null){
    		if(vystup.contains("leader_title") ){
    			String[] parts = vystup.split("leader_title");
    			vystup = parts[0]; 
    		}
    		
    		vystup =pomoc.ocisti_retazec(vystup, "government_type");
    		
    		String[] rozdelovac = {"!!"};
    		if (vystup != null){
    		String upraveny = pomoc.priprav_pole(vystup);
    		String[] pole =  pomoc.rozdel_do_pola(upraveny,rozdelovac);
    		String a;		
    		for(int i=0;i<pole.length;i++){
    			pole[i]  = pomoc.posledna_medzera(pole[i]);
    			infobox.setGovernment_type(pole);
    		}
    		}
    		
     	    flag = true;
    	}

    	vystup = pomoc.PouziRegex("\\| ?currency ?= [A-Za-z0-9 _ =*.:?!()+-<>\\]\\[#@\\{}'`$%^&;<>,ֹציז|]+", vysledok);  	
    	if (vystup != null){
    		if(vystup.contains("currency_code") ){
    			String[] parts = vystup.split("currency_code");
    			vystup = parts[0]; 
    		}
    		vystup =pomoc.ocisti_retazec(vystup, "currency");
    		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-]","");
    		vystup = vystup.replaceAll("  "," ");
    		vystup = pomoc.posledna_medzera(vystup);
    	    infobox.setCurrency(vystup);
     	    flag = true;
    	}
    	counter++;
    	return flag;
    }
    
    //funkcia sa zavola ked sa najde koniec elementu
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	boolean flag_country = false;
    	
    	//ak najdem koniec
        if (bTitle) {
        	//do premennej vysledok si ulozim vyparsovany infobox
        	String vysledok = sb.toString();
        	//zarovnanie do jedneho riadku
        	vysledok = vysledok.replaceAll("(\r\n|\n)", " "); 
        	//odstranenie nepotrebnych medzier
        	vysledok = vysledok.trim().replaceAll(" +", " "); 
        	//vybratie infoboxu country
        	String text = pomoc.PouziRegex("\\{\\{Infobox country \\s*(.*)", vysledok);
        	
        	//spustim vyparsovanie jednotlivych atributov z infoboxu
        	if (text !=null){
        	flag_country  = oparsujCountry(flag_country, text);
        	}
        	//ak som nieco vyparsoval...
        	if (flag_country == true){
        		//infobox country musi obsahovat aspon title alebo common name inak ho ignorujem ....
        		if ( infobox.getTitle()!= null ||  infobox.getCommon_name() != null){
        			//ak obsahuje tak ho pridam do listu
        			infoboxList.add(infobox); 			
        		}
        		System.out.println(counter);
        	}	

            bTitle = false;
            flag_country = false;     	 
        }
    }
 
    // spracovavanie vstupu
    public void characters(char ch[], int start, int length) throws SAXException {
         if (sb!=null && bTitle) {
             for (int i=start; i<start+length; i++) {
            	 sb.append(ch[i]);
             }
             
         }
         
    }
    


}
