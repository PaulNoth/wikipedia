package ner_dictionary;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ner_dictionary.indexing.Indexer;
import ner_dictionary.rules.CategorySet;
import ner_dictionary.rules.Rule;
import ner_dictionary.rules.RuleSet;
import ner_dictionary.rules.RuleType;

public class WikiXMLParserHandler extends DefaultHandler{

	private StringBuilder sBuilder;
	private String actualPageTitle;
	private String actualPageText;
	private boolean readText = false;
	private boolean redirect = false;
	private PrintStream outputFileStream;
	private RuleSet ruleSet;
	private Indexer indexer;
	public static final String SEPARATOR = "\t";
	private static final String INFOBOX_STR = "{{Infobox";
	private static final int INFOBOX_STR_LGTH = INFOBOX_STR.length();
	private Set<Redirect> redirectCache;
	private Map<String, Integer> resolvedPages;
	
	public WikiXMLParserHandler(PrintStream outputFileStream, RuleSet ruleSet, Indexer indexer) {
		super();
		this.outputFileStream = outputFileStream;
		this.ruleSet = ruleSet;
		this.indexer = indexer;
		// Redirects which are not resolved yet, are stored here
		this.redirectCache = new HashSet<Redirect>();
		// The wikipedia pages which categories are already resolved are stored here
		this.resolvedPages = new HashMap<String, Integer>();
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// React only to following wikipedia XML tags
		switch (qName) {
		case "title":
			sBuilder = new StringBuilder();
			readText = true;
			break;
		case "redirect":
			// Redirect found add to chace and set flag to not parse this pages text
			redirect = true;
			addRedirect(actualPageTitle, attributes.getValue(0));
			break;
		case "text":
			if (!redirect) {
				sBuilder = new StringBuilder();
				readText = true;
			}
			break;
		default:
			break;
		}
		super.startElement(uri, localName, qName, attributes);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		case "title":
			actualPageTitle = sBuilder.toString();
			readText = false;
			break;
		case "text":
			if (!redirect) {
				actualPageText = sBuilder.toString();
				readText = false;
				addEntry(actualPageTitle, actualPageText);
			} else {
				redirect = false;
			}
			break;
		default:
			break;
		}
		super.endElement(uri, localName, qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (readText) {
			sBuilder.append(ch,start,length);
		}
		super.characters(ch, start, length);
	}
	
	private void addEntry(String title, String text) {
		// detect page category depending on its text content
		Integer categoryId = detectCategoryId(text);
		// There are three possible storage places for resolved pages: index(with stored fields), output file(this is not necessary) and temporary cache(resolvedPages)
		// Storage in the index directory is the most important, because future querying is performed on it
		indexer.indexPage(title, categoryId);
		resolvedPages.put(title, categoryId);
		outputFileStream.println(title + SEPARATOR + categoryId);
	}
	
	private Integer detectCategoryId(String text) {
		// Apply each rule sequentially until a sufficient one is found
		for (Rule rule : ruleSet.getRuleList()) {
			// Regular expression
			Pattern pattern = Pattern.compile(rule.getPattern());
			// Determine if the type of the rule is Infobox type (if yes search only in Infobox)
			if (rule.verifyTypeById(RuleType.INFO)) {
				String infoboxText = parseInfobox(text);
				if(infoboxText == null) continue;	// No Infobox, continue with next rule
				Matcher matcher = pattern.matcher(infoboxText);
				if (matcher.find()) {
					return rule.getCategoryId();
				}
			} else {
				Matcher matcher = pattern.matcher(text);
				if (matcher.find()) {
					return rule.getCategoryId();
				}
			}
		}
		return CategorySet.DEFAULT_CATEGORY_ID;
	}
	
	private void addRedirect(String title, String redirectTitle) {
		redirectCache.add(new Redirect(title, redirectTitle));
	}
	
	public int processRedirects() {
		int cacheSize = redirectCache.size();
		System.out.println("Redirects: " + cacheSize);
		for (Redirect redirect : redirectCache) {
			Integer redirectCategoryId = resolvedPages.get(redirect.redirectPage);
			if (redirectCategoryId != null) {
				// Index and save to output file (and to the index directory)
				indexer.indexPage(redirect.page, redirectCategoryId);
				outputFileStream.println(redirect.page + SEPARATOR + redirectCategoryId);
				cacheSize--;
			}
		}
		return cacheSize;
	}
	
	public class Redirect
	{
		public String page;
	    public String redirectPage;
		
	    public Redirect(String page, String redirectPage) {
			this.page = page;
			this.redirectPage = redirectPage;
		}
	}
	
	private String parseInfobox(String text) {
		// This is part of wikixmlj project
		int startPos = text.indexOf(INFOBOX_STR);
		if(startPos < 0) return null;
		int bracketCount = 2;
	    int endPos = startPos + INFOBOX_STR_LGTH;
	    for(; endPos < text.length(); endPos++) {
	      switch(text.charAt(endPos)) {
	        case '}':
	          bracketCount--;
	          break;
	        case '{':
	          bracketCount++;
	          break;
	        default:
	      }
	      if(bracketCount == 0) break;
	    }
		return text.substring(startPos, endPos+1);
	}
}
