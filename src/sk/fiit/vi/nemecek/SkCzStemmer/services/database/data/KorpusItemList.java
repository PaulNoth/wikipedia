package sk.fiit.vi.nemecek.SkCzStemmer.services.database.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "letters")
@XmlAccessorType (XmlAccessType.FIELD)
public class KorpusItemList {

    @XmlElement(name = "letter")
    private List<KorpusItem>  list;
    
    public void addItem(KorpusItem item){
        this.list.add(item);
    }
    
    public List<KorpusItem> getList(){
        return list;
    }
    
    public KorpusItemList(){
        this.list = new ArrayList<>();
    }
}
