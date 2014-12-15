package ner_dictionary.rules;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class CategorySet {
	
	public static final Integer DEFAULT_CATEGORY_ID = 0;
	public static final String DEFAULT_CATEGORY_NAME = "Miscellaneous";
	private Map<String, Integer> categories;
	private Map<Integer, String> invertedCategories;
	private Integer lastId = 0;
	
	public CategorySet(RuleSet ruleSet) {
		// Detect all categories, which occurred in rules, assign an id to each
		categories = new HashMap<String, Integer>();
		for (Rule rule : ruleSet.getRuleList()) {
			Integer value = categories.get(rule.getCategoryName());
			if (value == null) {
				categories.put(rule.getCategoryName(), ++lastId);
				rule.setCategoryId(lastId);
			} else {
				rule.setCategoryId(value);
			}
		}
	}
	
	public CategorySet(String filePath) {
		// Create category set from file
		invertedCategories = new HashMap<Integer, String>();
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(filePath);
			prop.load(input);
			Set<Object> set = prop.keySet();
			for (Iterator<Object> iterator = set.iterator(); iterator.hasNext();) {
				String entry = (String) iterator.next();
				String property = prop.getProperty(entry);
				if (property == null) {
					System.err.println("Category mapping file is corrupted!");
				} else {
					invertedCategories.put(Integer.valueOf(property), entry);
				}
			}
		} catch (IOException e) {
			System.err.println("The set of categories could not be loaded, check if the path to the category mapping file is set correctly!.");
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Map<Integer, String> getCategoryMapping() {
		return this.invertedCategories;
	}
	
	public void saveSetToFile(String filePath) {
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream(filePath);
			prop.setProperty(DEFAULT_CATEGORY_NAME, DEFAULT_CATEGORY_ID.toString());
			for (Map.Entry<String, Integer> entry : categories.entrySet()) {
				prop.setProperty(entry.getKey(), entry.getValue().toString());
			}
			prop.store(output, null);
		} catch (IOException e) {
			System.err.println("The set of categories could not be saved in file.");
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
