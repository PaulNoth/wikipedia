package sk.fiit.nemecek.jolana.server.services.database.data;

import java.io.Serializable;


public class AlphabetItem implements Serializable {

    private long id;
    
    public AlphabetItem() {
        super();
    }
    private String letter;
    
    public AlphabetItem (String letter){
        this.letter=letter;
    }

    public long getId() { return id;}
    public void setId(long id) { this.id = id;}

    public String getLetter() { return letter;}
    public void setLetter(String letter) { this.letter = letter;}
    
    @Override
    public String toString() { return "AlphabetItem [letter=" + letter + "]"; }
}
