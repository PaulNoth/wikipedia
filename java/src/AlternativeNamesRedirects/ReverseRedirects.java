package xonder_VINF;

public class ReverseRedirects {
	public int count;   // count of name redirects
	public String reversers;  // all name redirects
	public boolean exists;    // if it exist, or it is a dead link
	public String disambgis;   // all disambigous pages titles
	public String deadDisambigs; // all dead links in disambigous pages titles
	public int countDisam;   // all of disambigous references
	public boolean isDis;   // is disambigous page?
	public String uniq;     // all uniqe redirects, not used
	
	public ReverseRedirects(){
		count = 0;
		countDisam = 0;
		isDis = false;
	} // explicit constructor with existing link parameter, used always
	public ReverseRedirects(boolean ex){
		count = 0;
		countDisam = 0;
		exists = ex;
		isDis = false;
	}
}
