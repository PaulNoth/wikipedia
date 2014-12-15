package infoboxNames;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoboxParser {

	// parsing infobox name
	public static Article parseArticleInfoboxType(Article article) {

		
		//infobox first type {{infobox
		Pattern p1 = Pattern.compile("(?i)\\{\\{(\\s)*infobox(\\s)([^<|\\[\\n\\}\\{]*)");
			
		Matcher m1 = p1.matcher(article.getText());
			
		while( m1.find()){
			article.insertInfoboxType(m1.group(3).toString().replaceAll("\\s+$", ""));
		}
		
		//infobox second type infobox}}
		Pattern p2 = Pattern.compile("(?i)\\{\\{([^|\\{]*)\\s+infobox(\\s)*\\}\\}");
		
		Matcher m2 = p2.matcher(article.getText());
			
		while( m2.find()){
			article.insertInfoboxType(m2.group(1).toString().replaceAll("\\s+$", ""));
		}

		return article;
	}
	
	//parsing infobox property - population_estimate or musical genre
	public static Article parseArticleInfoboxProperty(Article article){

		String population;
		//finding of population estimate which is part of article in infobox country
		if(article.getInfoboxType().contains("country")){
			
			Pattern p3 = Pattern.compile("(population_estimate(\\s)*=(\\s)*([0-9]|[,.]|((\\s)?million)|\\s?)*)");
			Matcher m3 = p3.matcher(article.getText());
				
			while( m3.find()){
				population = m3.group(1).toString();
				if(population.contains("million")){
					if(population.matches("population_estimate\\s*=\\s*[0-9]*(\\.|\\,)[0-9][0-9]\\s*million")){
						population = population.replaceAll("million", "0000");
					}
					if(population.matches("population_estimate\\s*=\\s*[0-9]*(\\.|\\,)[0-9]\\s*million")){
						population = population.replaceAll("million", "00000");
					}
					if(population.matches("population_estimate\\s*=\\s*[0-9]*\\smillion")){
						population = population.replaceAll("million", "000000");
					}
				}
				article.setInfoboxType(article.getInfoboxType() + "\n" + article.getTitle() + "\t" +  population
						.replaceAll("\\.|,|\\s*", "").replaceAll("=", " = "));
			}
		}
		
		
		//finding of genre which is part of article in infobox musical artist
		if(article.getInfoboxType().contains("musical artist")){
			
			Pattern p4 = Pattern.compile("(?s)(\\|\\s*genre\\s*=(.+?)((\\n\\|)|(\\n\\}\\})))");	//pattern for more lines text		
			Matcher m4 = p4.matcher(article.getText());
		
			String genre = "";
			
			while( m4.find()){
				genre = m4.group(1).toString().replaceAll("\\s+", " ");
			}
			
			
			Pattern p5 = Pattern.compile("(\\[\\[[-\\p{L}0-9\\s\\|]*\\]\\])");
			Matcher m5 = p5.matcher(genre);
			
			while( m5.find()){
				article.insertInfoboxProperty(m5.group(1).toString().replaceAll("\\s+", " ").replaceAll("\\[|\\]", ""));
			}
			
			if(!article.getInfoboxProperty().isEmpty()){
				
				//adding infobox property to string after infobox type
				article.setInfoboxType(article.getInfoboxType() + "\n" + article.getTitle() + "\tgenre = " + article.getInfoboxProperty());
			}
		}
		
		return article;
	}
	
	
}
