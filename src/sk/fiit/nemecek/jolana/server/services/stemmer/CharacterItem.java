package sk.fiit.nemecek.jolana.server.services.stemmer;
/**
 * Class for temporar saving result of preprocessing for each letter in word.
 * @author Tomáš Nemeèek
 *
 */
public class CharacterItem {

    private double precision;
    private String character;

    public CharacterItem(String s, double entrophy) {
        this.precision = entrophy;
        this.character = s;
    }
    
    public CharacterItem clone(){
        CharacterItem i = new CharacterItem(this.character,this.precision);
        return i;
    }

    @Override
    public String toString() {
        return "Word [precision=" + precision + ", character=" + character + "]";
    }

    public double getPrecision() {
        return precision;
    }
    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public String getCharacter() {
        return character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }
}
