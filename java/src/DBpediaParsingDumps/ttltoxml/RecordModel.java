package ttltoxml;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

/**
 * @author Skrisa Július
 *
 * RecordModel represents ttl record. When the file is parsed records are saving into this data strucutre.
 */
public class RecordModel {
	private String resource;
	private String property;
	private String value;
	
	public boolean setAttributes(Triple stmt){ // divide triple data to 3 objects 
	    Node   object    = stmt.getObject();    
	    Node r = stmt.getSubject();
	    Node p = stmt.getPredicate();
	    if(!SettingsOfTtlFiles.Predicates.contains(p.getLocalName())) // if predicate is acceptable 
	    		return false;
	    if (!object.isLiteral()) { 
	    	if(object.toString().contains("resource")) // if there is resource as object
	    		this.setValue(object.getLocalName());
	    	else
	    		this.setValue(object.toString());
	     } else {
	    	 this.setValue(object.getLiteralValue().toString()); // saving value of triple
	     }
	    
	    this.setProperty(p.getLocalName()); // saving predicate of triple
	    this.setResource(r.getLocalName()); // saving resource of triple
	    return true;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	
}
