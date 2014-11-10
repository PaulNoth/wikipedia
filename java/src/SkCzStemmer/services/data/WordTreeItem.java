package skCzStemmer.services.data;

import java.util.ArrayList;
import java.util.List;

public class WordTreeItem {

    private char itemId;
    
    private String letter;
    
    private WordTreeItem parent;
    
    private List<WordTreeItem> children;
    
    private boolean end;
    
    private long probability;
    
    public WordTreeItem(){
        this.children = new ArrayList<WordTreeItem>();
        this.probability = 0;
        this.end = false;
    }
    
    public char getId() { return itemId;}
    
    public String getPismeno() { return letter;}
    public void setPismeno(String pismeno) { this.letter = pismeno; this.itemId=pismeno.charAt(0);}
    
    public long getProbability() { return probability;}
    public void setProbability(long probability) { this.probability = probability;}
    
    public boolean getEnd() { return end;}
    public void setEnd(boolean isEnd) { this.end = isEnd;}

    public List<WordTreeItem> getChildren() { return children;}
    public void addKorpusItem(WordTreeItem item){ this.children.add(item);}
    
    public WordTreeItem getParent() { return parent;}
    public void setParent(WordTreeItem parent) { this.parent = parent;}
    
    public void deepCopy(WordTreeItem item) {
        this.itemId = item.getId();
        this.letter = item.getPismeno();
        this.children = item.getChildren();
        this.end = item.getEnd();
        this.probability = item.getProbability();
    }
    
    @Override
    public String toString() {
        return "KorpusItem [letter=" + letter + "]";
    }
}
