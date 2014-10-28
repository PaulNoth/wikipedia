package skCzStemmer.services.database.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "letter")
@XmlAccessorType (XmlAccessType.FIELD)
public class KorpusItem implements Serializable {

    @XmlAttribute
    private char itemId;
    
    @XmlAttribute
    private String letter;
    
    @XmlElement(name = "childLetters")
    private KorpusItemList items;
    
    @XmlAttribute
    private boolean end;
    
    @XmlAttribute
    private long probability;
    
    public KorpusItem(){
        this.items = new KorpusItemList();
        this.probability = 1;
        this.end = false;
    }
    
    public char getId() { return itemId;}
    
    public String getPismeno() { return letter;}
    public void setPismeno(String pismeno) { this.letter = pismeno; this.itemId=pismeno.charAt(0);}
    
    public long getProbability() { return probability;}
    public void setProbability(long probability) { this.probability = probability;}
    
    public boolean getEnd() { return end;}
    public void setEnd(boolean isEnd) { this.end = isEnd;}

    public KorpusItemList getChildren() { return items;}
    public void addKorpusItem(KorpusItem item){ this.items.addItem(item);}
    
    public void deepCopy(KorpusItem item) {
        this.itemId = item.getId();
        this.letter = item.getPismeno();
        this.items = item.getChildren();
        this.end = item.getEnd();
        this.probability = item.getProbability();
    }
    
    @Override
    public String toString() {
        return "KorpusItem [letter=" + letter + "]";
    }
}
