package enwikiPagerank.pagerankComputation;

public class PagerankDataObject {
	
	private String id;
	private String[] referencedIds;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getReferencedIds() {
		return referencedIds;
	}
	public void setReferencedIds(String[] referencedIds) {
		this.referencedIds = referencedIds;
	}
	
	

}
