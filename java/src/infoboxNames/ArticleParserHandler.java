package infoboxNames;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//handler for parsing xml wikipedia file
public class ArticleParserHandler extends DefaultHandler
{
	
	private StringBuilder infoboxList= new StringBuilder();
	
	private Stack elementStack = new Stack();
	
	private Stack objectStack = new Stack();
	
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		this.elementStack.push(qName);
		
		//if starting element is page, then we create new article
		if("page".equals(qName))
		{
			Article article = new Article();
			
			article.appendText("");
			
			this.objectStack.push(article);
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		this.elementStack.pop();
		
		//if ending element is page, then we handle and parse data which we set in article
		if ("page".equals(qName))
		{
			Article object = (Article) this.objectStack.pop();
			
			//System.out.println(object.getTitle());
			
			object = InfoboxParser.parseArticleInfoboxType(object);
			
			//if infobox contains some specific type of infobox in article we find specific property
			if(!object.getInfoboxType().isEmpty() &&
					(object.getInfoboxType().contains("country") || object.getInfoboxType().contains("musical artist")) ){
				object = InfoboxParser.parseArticleInfoboxProperty(object);
			}
			
			object.removeText();
			
			//if we have some type (with property if exist added to type string) of infobox in article we add article to our infoboxList
			if(!object.getInfoboxType().isEmpty()){
				infoboxList.append(object.getTitle() + "\t" + object.getInfoboxType() + "\n");
			}

		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String value = new String(ch, start, length);
		
		if (value.length() == 0){
			return;
		}
		
		//if actual element is title then set title value to article or if element is text then set text value to article
		if("title".equals(currentElement()))
		{
			Article article = (Article) this.objectStack.peek();
			article.setTitle(value);
		}
		else if ("text".equals(currentElement()))
		{
			Article article = (Article) this.objectStack.peek();
			
			article.appendText(value);

		}
	}
	
	private String currentElement()
	{
		return (String) this.elementStack.peek();
	}
	

	public StringBuilder getInfoboxes()
	{
		return infoboxList;
	}
	
}
