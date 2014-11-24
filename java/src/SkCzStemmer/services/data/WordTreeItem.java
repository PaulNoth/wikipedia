package skCzStemmer.services.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tree")
@XmlAccessorType (XmlAccessType.FIELD)
/**
 * Suffix tree item used in stemming process.
 * @author Tomas Nemecek
 */
public class WordTreeItem {

    @XmlAttribute
    private char itemId;

    @XmlAttribute
    private String letter;
    
    @XmlElement(name = "childLetters")
    private List<WordTreeItem> children;
    
    private WordTreeItem parent;
    
    @XmlAttribute
    private boolean end;
    
    @XmlAttribute
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

    public WordTreeItem getParent() {
        return parent;
    }

    public void setParent(WordTreeItem parent) {
        this.parent = parent;
    }
}