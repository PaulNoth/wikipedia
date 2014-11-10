import java.awt.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Page {
	public String title;
	ArrayList<SubPage> subPages;
	
	
	public void parseDisambigDescription(String text)
	{
		this.subPages = new ArrayList<SubPage>();
		  
		  Pattern pattern = Pattern.compile("\\* *\\[\\[.*");
		  Matcher matcher = pattern.matcher(text);
		  
		  String tempItem;
		  
		  while(matcher.find())
		  {
			  SubPage subPage = new SubPage();
			  
			  tempItem = matcher.group();
			  
			  tempItem = tempItem.replace("*","");
			  tempItem = tempItem.replace("[[","");
			  tempItem = tempItem.replace("]]","");
			  
			  String[] element = tempItem.split(",");
			  
			  if(element.length >= 1)
			  {
				  subPage.subtitle = element[0].trim();
			  }
			  else
			  {
				  subPage.subtitle = null;
			  }
			  
			  if(element.length >= 2)
			  {
				  subPage.description = element[1].trim();
			  }
			  else
			  {
				  subPage.description = null;
			  }
			  			  
			  
			  this.subPages.add(subPage);
		  }
	}
}
