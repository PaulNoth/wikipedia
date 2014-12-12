package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerPerson  extends DefaultHandler {
	
	//zoznam najdenych infoboxov typu person
	private List<Infobox_person> infoboxPersonList = null;
	//aktualne spracovavany infobox
	private Infobox_person infoboxPerson = null;
	private StringBuffer sb;
	//pocitadlo
	int counter = 0;
	public List<Infobox_person> getInfoboxList() {
        return infoboxPersonList;
    }
	Help pomoc = new Help();
	boolean bTitle = false;
	
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
    	sb = new StringBuffer();
    	//ak najdem page vytvorim novy infobox
        if (qName.equalsIgnoreCase("Page")) {                
        	infoboxPerson = new Infobox_person();
            if (infoboxPersonList == null) {
            	infoboxPersonList = new ArrayList<>();
            }
          //vyparsovanie atributu text
        } else if (qName.equalsIgnoreCase("text")) {
        	bTitle = true;
        } 
    }
    
    //metoda na oparsovanie infoboxu, v atribute vysledok je ulozeny text na spracovanie
    public boolean oparsujPerson(String vysledok){
    	boolean flag = false;
    	//postupne vyberanie jednotlivych atributov z textu
    	String vystup;
    	//zoznam regexov ktory sa pouzije na ziskanie vacsiny atributov
    	List<String> list_regexov = new ArrayList<String>();
    	List<String> list_atributov = new ArrayList<String>();
    	
    	list_regexov.add("\\| ?name ?= [^|]+");
    	list_regexov.add("\\| ?image_size ?= [^|]+");
    	list_regexov.add("\\| ?image ?= [^|]+");
    	list_regexov.add("\\| ?birth_place ?= [^|]+");
    	list_regexov.add("\\| ?death_place ?= [^|]+");
    	
    	list_atributov.add("name");
    	list_atributov.add("image_size");
    	list_atributov.add("image");
    	list_atributov.add("birth_place");
    	list_atributov.add("death_place");

    	for(int i=0;i<list_regexov.size();i++){
    		vystup = pomoc.PouziRegex(list_regexov.get(i), vysledok);  
        	if (vystup != null){
        		vystup =pomoc.ocisti_retazec(vystup, list_atributov.get(i));
        		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-]","");
        		vystup = vystup.replaceAll("  "," ");
        		vystup = pomoc.posledna_medzera(vystup);
        		
        		if (list_atributov.get(i).contains("name")){
        			infoboxPerson.setName(vystup);
        		}
        		else if(list_atributov.get(i).contains("image_size")){
        			infoboxPerson.setImage_size(vystup);
        		}
        		else if(list_atributov.get(i).contains("image")){
        			infoboxPerson.setImage(vystup);
        		}
        		else if(list_atributov.get(i).contains("birth_place")){
        			infoboxPerson.setBirth_place(vystup);
        		}
        		else if(list_atributov.get(i).contains("death_place")){
        			infoboxPerson.setDeath_place(vystup);
        		}
        		
        	    flag = true;
        	}
    	}
   
    	vystup = pomoc.PouziRegex("\\| ?birth_date ?= [^}]+", vysledok);  	
    	if (vystup != null){
    		
    		if(vystup.contains("birth_place")){
    			String[] parts = vystup.split("birth_place");
    			vystup = parts[0]; 
    		}
    		
    		if (vystup.contains("{")){
    			String pomocna = pomoc.ocisti_retazec(vystup, "birth_date");
    			pomocna = pomocna.replaceAll("[^0-9\\|]","");
    			String rok="";
    			String den="";
    			String mesiac="";
    			
    			if(pomocna.length()>1){
    			
    			String a = pomocna.substring(0, 1);
    			String b = pomocna.substring(1, 2);
    			
    			if (b.contains("|") && a.contains("|")){
    				pomocna = pomocna.substring(2, pomocna.length());
    			}
    			
    			else if ( a.contains("|")){
    				pomocna = pomocna.substring(1, pomocna.length());
    			}
    			}
    			String[] parts = pomocna.split("\\|");
    			for (int i = 0;i<parts.length;i++){
    				
    					if (rok =="" && parts[i]!=""){
    						rok = parts[i];
    						continue;
    					}
    					if (mesiac =="" && parts[i]!=null){
    						mesiac = parts[i];
    						continue;
    					}	
    					if (den == "" && parts[i]!=null){
    						den = parts[i];
    						break;
    					}
    				
    			}
    			
    			if(pomocna != null){
    			   			    		
    			if (rok !=null){
    					
    			infoboxPerson.setBirth_year(rok);
    			}
    			if (mesiac !=null){
    			infoboxPerson.setBirth_month(mesiac);
    			}
    			if (den !=null){
    			infoboxPerson.setBirth_day(den);
    			}
    			}
    			
    			infoboxPerson.setBirth_date("ZNAMY FORMAT");
    			
    		}
    		else{
    			infoboxPerson.setBirth_year("ZLY FORMAT DATUMU!");
    			infoboxPerson.setBirth_month("ZLY FORMAT DATUMU!");
    			infoboxPerson.setBirth_day("ZLY FORMAT DATUMU!");
    			infoboxPerson.setBirth_date(vystup);
    		}
    	    flag = true;
    	}
    	
    
    	vystup = pomoc.PouziRegex("\\| ?death_date ?= [^}]+", vysledok);  	
    	    	
    	if (vystup != null){
    		
    		if(vystup.contains("death_place")){
    			String[] parts = vystup.split("death_place");
    			vystup = parts[0]; 
    		}
    		
    		if (vystup.contains("{")){
    			String pomocna = pomoc.ocisti_retazec(vystup, "death_date");
    			pomocna = pomocna.replaceAll("[^0-9\\|]","");
    			String rok="";
    			String den="";
    			String mesiac="";
    			
    			if(pomocna.length()>1){
    			String a = pomocna.substring(0, 1);
    			String b = pomocna.substring(1, 2);
    			
    			if (b.contains("|") && a.contains("|")){
    				pomocna = pomocna.substring(2, pomocna.length());
    			}
    			
    			else if ( a.contains("|")){
    				pomocna = pomocna.substring(1, pomocna.length());
    			}
    			}
    			String[] parts = pomocna.split("\\|");
    			for (int i = 0;i<parts.length;i++){
    				
    					if (rok =="" && parts[i]!=""){
    						rok = parts[i];
    						continue;
    					}
    					if (mesiac =="" && parts[i]!=null){
    						mesiac = parts[i];
    						continue;
    					}	
    					if (den == "" && parts[i]!=null){
    						den = parts[i];
    						break;
    					}
    				
    			}
    			
    			if(pomocna != null){
    			   			    		
    			if (rok !=null){
    					
    			infoboxPerson.setDeath_year(rok);
    			}
    			if (mesiac !=null){
    			infoboxPerson.setDeath_month(mesiac);
    			}
    			if (den !=null){
    			infoboxPerson.setDeath_day(den);
    			}
    			}
    			
    			infoboxPerson.setDeath_date("ZNAMY FORMAT");
    			
    		}
    		else{
    			infoboxPerson.setDeath_year("ZLY FORMAT DATUMU!");
    			infoboxPerson.setDeath_month("ZLY FORMAT DATUMU!");
    			infoboxPerson.setDeath_day("ZLY FORMAT DATUMU!");
    			infoboxPerson.setDeath_date(vystup);
    		}
    		
    	    flag = true;
    	}

    	counter++;
      	return flag;
    }
    
    //funkcia sa zavola ked sa najde koniec elementu
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	boolean flag_person = false;
    	
    	//ak najdem koniec
        if (bTitle) {
          
        	//do premennej vysledok si ulozim vyparsovany infobox
        	String vysledok = sb.toString();
        	//zarovnanie do jedneho riadku
        	vysledok = vysledok.replaceAll("(\r\n|\n)", " "); 
        	//odstranenie nepotrebnych medzier
        	vysledok = vysledok.trim().replaceAll(" +", " "); 
        	//vybratie kompletneho infoboxu person
        	String text = pomoc.PouziRegex("\\{\\{Infobox person \\s*(.*)", vysledok);
        
        	//spustim vyparsovanie jednotlivych atributov z infoboxu
        	if (text !=null){
        		flag_person  = oparsujPerson(text);
        	}
        	//ak som nieco vyparsoval...
        	if (flag_person == true){
        		//infobox person musi obsahovat aspon name ....
        		if ( infoboxPerson.getName()!= null){
        			//ak obsahuje tak ho pridam do listu
        			infoboxPersonList.add(infoboxPerson);    			
        		}
        		System.out.println(counter);
        	}	
            bTitle = false;
            flag_person = false;     	 
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