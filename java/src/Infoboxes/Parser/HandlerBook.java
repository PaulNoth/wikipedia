package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerBook  extends DefaultHandler {
	
	//zoznam najdenych infoboxov typu person
	private List<Infobox_book> infoboxBookList = null;
	//aktualne spracovavany infobox
	private Infobox_book infoboxBook = null;
	private StringBuffer sb;
	//pocitadlo
	int counter = 0;
	Help pomoc = new Help();
	public List<Infobox_book> getInfoboxList() {
        return infoboxBookList;
    }

	boolean bTitle = false;
	
 
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
    	sb = new StringBuffer();
    	//ak najdem page vytvorim novy infobox
        if (qName.equalsIgnoreCase("Page")) {                
        	infoboxBook = new Infobox_book();
            if (infoboxBookList == null) {
            	infoboxBookList = new ArrayList<>();
            }
        } 
        //vyparsovanie atributu text
        if (qName.equalsIgnoreCase("text")) {
        	bTitle = true;
        } 
    }
 
    //metoda na oparsovanie infoboxu, v atribute vysledok je ulozeny text na spracovanie
    public boolean oparsujBook(String vysledok){
    	boolean flag = false;
    	//zoznam regexov ktory sa pouzije na ziskanie vacsiny atributov
    	List<String> list_regexov = new ArrayList<String>();
    	list_regexov.add("\\| ?name ?= [^|]+");
    	list_regexov.add("\\| ?translator ?= [^|]+");
    	list_regexov.add("\\| ?image ?= [^|]+");
    	list_regexov.add("\\| ?caption ?= [^|]+");
    	list_regexov.add("\\| ?author ?= [^|]+");
    	list_regexov.add("\\| ?country ?= [^|]+");
    	list_regexov.add("\\| ?subject ?= [^|]+");
    	list_regexov.add("\\| ?genre ?= [^|]+");
    	list_regexov.add("\\| ?published ?= [^|]+");
    	list_regexov.add("\\| ?isbn ?= [^|]+");
    	list_regexov.add("\\| ?preceded_by ?= [^|]+");
    	list_regexov.add("\\| ?followed_by ?= [^|]+");
    	
    	List<String> list_atributov = new ArrayList<String>();
    	list_atributov.add("name");
    	list_atributov.add("translator");
    	list_atributov.add("image");
    	list_atributov.add("caption");
    	list_atributov.add("author");
    	list_atributov.add("country");
    	list_atributov.add("subject");
    	list_atributov.add("genre");
    	list_atributov.add("published");
    	list_atributov.add("isbn");
    	list_atributov.add("preceded_by");
    	list_atributov.add("followed_by");
    	
    	String vystup;
    	for(int i=0;i<list_regexov.size();i++){
    	    vystup = pomoc.PouziRegex(list_regexov.get(i), vysledok);  	
        	if (vystup != null){
        		vystup = pomoc.ocisti_retazec(vystup, list_atributov.get(i));
        		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-]","");
        		vystup = pomoc.posledna_medzera(vystup);
        		
        			if(list_atributov.get(i).contains("name")){
        				infoboxBook.setName(vystup);
        			}
        			else if(list_atributov.get(i).contains("translator")){
        				infoboxBook.setTranslator(vystup);
        			}
        			else if(list_atributov.get(i).contains("image")){
        				infoboxBook.setImage(vystup);
        			}
        			else if(list_atributov.get(i).contains("captation")){
        				infoboxBook.setCaption(vystup);
        			}
        			else if(list_atributov.get(i).contains("author")){
        				infoboxBook.setAuthor(vystup);
        			}
        			else if(list_atributov.get(i).contains("country")){
        				infoboxBook.setCountry(vystup);
        			}
        			else if(list_atributov.get(i).contains("subject")){
        				infoboxBook.setSubject(vystup);
        			}
        			else if(list_atributov.get(i).contains("published")){
        				infoboxBook.setPublished(vystup);
        			}
        			else if(list_atributov.get(i).contains("pages")){
        				infoboxBook.setPages(vystup);
        			}
        			else if(list_atributov.get(i).contains("isbn")){
        				infoboxBook.setIsbn(vystup);
        			}
        			else if(list_atributov.get(i).contains("preceded_by")){
        				infoboxBook.setPreceded_by(vystup);
        			}
        			else if(list_atributov.get(i).contains("followed_by")){
        				infoboxBook.setFollowed_by(vystup);
        			}
        	    flag = true;
        	}
    	}
    	
    	//postupne vyberanie jednotlivych atributov z textu
    	
    	vystup = pomoc.PouziRegex("\\| ?language ?= [A-Za-z0-9 _ =*.:?!()+-<>\\]\\[#@\\{}'`$%^&;<>,ֹציז|]+", vysledok);  	
    	if (vystup != null){
    		
    		if(vystup.contains("series") ){
    			String[] parts = vystup.split("series");
    			vystup = parts[0]; 
    		}
    		
    		if(vystup.contains("subject") ){
    			String[] parts = vystup.split("subject");
    			vystup = parts[0]; 
    		}
    		
    		if(vystup.contains("genre") ){
    			String[] parts = vystup.split("genre");
    			vystup = parts[0]; 
    		}

    		vystup = pomoc.ocisti_retazec(vystup, "language");
    		vystup = vystup.replaceAll("\\|"," ");
    		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-]|","");
    		vystup = pomoc.posledna_medzera(vystup);
    		infoboxBook.setLanguage(vystup);
    	    flag = true;
    	}
    	
    	vystup = pomoc.PouziRegex("\\| ?genre ?= [^|]+", vysledok);  	
    	if (vystup != null){
    		vystup = pomoc.ocisti_retazec(vystup, "genre");
    		vystup = vystup.replaceAll("[^0-9a-zA-Z.:,?! +-\\[\\];\\/*]","");
    		String[] rozdelovac = {",",";","*"};
    		if (vystup != null){
    		String[] pole =  pomoc.rozdel_do_pola(vystup,rozdelovac);
    		String a;		
    		for(int i=0;i<pole.length;i++){
    			pole[i]  = pomoc.posledna_medzera(pole[i]);
    			infoboxBook.setGenre(pole);
    		}
    		}
    	    flag = true;
    	}
    	  
    	counter++;
    	return flag;
    }
    
    //funkcia sa zavola ked sa najde koniec elementu
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	boolean flag_book = false;
    	
    	//ak najdem koniec
        if (bTitle) {
        	//do premennej vysledok si ulozim vyparsovany infobox
        	String vysledok = sb.toString();
        	//zarovnanie do jedneho riadku
        	vysledok = vysledok.replaceAll("(\r\n|\n)", " "); 
        	//odstranenie nepotrebnych medzier
        	vysledok = vysledok.trim().replaceAll(" +", " ");
        	//vybratie infoboxu book
        	String text = pomoc.PouziRegex("\\{\\{Infobox book\\s*(.*)", vysledok);
        
        	//spustim vyparsovanie jednotlivych atributov z infoboxu
        	if (text !=null){
        		flag_book  = oparsujBook(text);
        	}
        	//ak som nieco vyparsoval...
        	if (flag_book == true){
        		//infobox book musi obsahovat aspon name a authora inak ho ignorujem ....
        		if ( infoboxBook.getName()!= null && infoboxBook.getAuthor() != null){
        			
        			//ak obsahuje tak ho pridam do listu
        			infoboxBookList.add(infoboxBook);    			
        		}
        		System.out.println(counter);
        	}	
            bTitle = false;
            flag_book = false;     	 
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
