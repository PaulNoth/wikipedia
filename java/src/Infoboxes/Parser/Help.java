package Parser;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//trieda obsahuje funkcie ktore sa casto pouzivaju pri parsovani zdrojoveho xml suboru

public class Help {
	//odstranenie prvej casti retazca
	public String ocisti_retazec(String ret, String Atribut){
		ret = ret.replace("|"+Atribut+" = ", "");
		ret = ret.replace("| "+Atribut+" = ", "");
		ret = ret.replace("|"+Atribut+"= ", "");
		ret = ret.replace("| "+Atribut+"= ", "");
		return ret;
	}
	
	 //Aplikacia regexu
	public String PouziRegex(String regex, String vstup){
	    	Pattern pattern = Pattern.compile(regex);
	    	Matcher matcher = pattern.matcher(vstup);
	    	if (matcher.find())
	    	{
	    		return matcher.group(0);
	    	}
	    	return null;
	 }
	
	//odstranenie poslednej medzery    
    public String posledna_medzera(String ret){
    	if (ret!=null && ret.length()>2){
    		String medzera= ret.substring(ret.length() - 1);
    		
    		if (medzera.contains(" ")){
    			ret = ret.substring(0,ret.length()-1);
    		}
    		}
		return ret;
    	
    }
    
    //rozdelenie stringu do pola na zaklade mnoziny rozdelovacov
    public String[] rozdel_do_pola(String ret, String[] rozdelovac){
    	String[] vysledok = new String[1];
    	String[] parts;
    	String pomoc;
    	for (int i =0; i<rozdelovac.length;i++){
    		if (ret.contains(rozdelovac[i])) {
    			pomoc = ret.replaceAll("[^|0-9a-zA-Z.:,?! +-]","");
    			parts = pomoc.split(rozdelovac[i]);
    			return  parts ;
        	}
    	}
    		String a = ret.replaceAll("[^|0-9a-zA-Z.:,?! +-]","");
    		Arrays.fill(vysledok, a);
    		return vysledok;
    }
    
    //funkcia na ocistenie retazca a pripravenie retazca na rozdelenie do pola
    public String priprav_pole(String ret){
    	int zatvorky = 0;
    	String pole = "";
    	for (int i = 0; i < ret.length();i++){
    		String a= Character.toString(ret.charAt(i));
    		String b = "";
    		if (i<ret.length()-1){
    		 b= Character.toString(ret.charAt(i+1));
    		}
    		if (a.contains("[") && b.contains("[") && zatvorky == 0){
    			zatvorky++;
    			continue;
    		}
    		
    		if ( a.contains("[") && zatvorky == 1){
    			zatvorky++;
    			continue;
    		}
    		
    		if (a.contains("]") && b.contains("]") && zatvorky == 2){
    			zatvorky--;
    			continue;
    		}
    		
    		if (a.contains("]")  && zatvorky == 1){
    			zatvorky--;
    			pole = pole.concat("!!");
    			continue;
    		}
    		
    		if (zatvorky == 2){
    			pole = pole.concat(Character.toString(ret.charAt(i)));
    			continue;
    		}

    	
    	}
    	return pole;
    	
    }
}
