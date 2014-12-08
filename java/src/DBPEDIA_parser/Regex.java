/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpedia_parser;

/**
 *
 * @author Zuzka
 */
public class Regex {

    //regexes
    private String regex_labels = "(?i)schema#label>[^\"]*\"([^\"]*)\"";
    private String regex_shortAbstract = "(?i)resource/([^>]*)>.*schema#comment>[^\"]*\"(.*)\"@en";
    private String regex_category = "(?i)resource/([^>]*)>.*Category:([^>]*)>";
    private String regex_redirect = "(?i)resource/([^>]*)>.*wikiPageRedirects[^<]*<http://dbpedia.org/resource/([^>]*)>";
    //filepaths
    private String filePath_labels = "input\\labels_en.ttl";
    private String filePath_shortAbstract = "input\\short_abstracts_en.ttl";
    private String filePath_A_shortAbstract = "input\\A_short_abstracts_en.ttl";
    private String filePath_categories = "input\\article_categories_en.ttl";
    private String filePath_A_categories = "input\\article_A_categories_en.ttl";
    private String filePath_redirects = "input\\redirects_en.ttl";
    
    public String get_regex_labels(){        
        return this.regex_labels;
    }
    
    public String get_regex_shortAbstract(){        
        return this.regex_shortAbstract;
    }
    
    public String get_regex_category(){        
        return this.regex_category;
    }
      
    public String get_regex_redirect(){        
        return this.regex_redirect;
    }
    
    public String get_filePath_labels(){        
        return this.filePath_labels;
    }
    
    public String get_filePath_shortAbstract(){        
        return this.filePath_shortAbstract;
    }
    
    public String get_filePath_A_shortAbstract(){        
        return this.filePath_A_shortAbstract;
    }
    
    public String get_filePath_category(){        
        return this.filePath_categories;
    }
    
    public String get_filePath_A_category(){        
        return this.filePath_A_categories;
    }
      
    public String get_filePath_redirect(){        
        return this.filePath_redirects;
    }


}
