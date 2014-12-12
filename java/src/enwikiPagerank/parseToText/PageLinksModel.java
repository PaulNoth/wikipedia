package enwikiPagerank.parseToText;
/**
 * Created by Jozef on 29.10.2014.
 */
public class PageLinksModel {

    private int plFrom;
    private long plNamespace;
    private String plTitle;


    public int getPlFrom() {
        return plFrom;
    }

    public void setPlFrom(int plFrom) {
        this.plFrom = plFrom;
    }

    public long getPlNamespace() {
        return plNamespace;
    }

    public void setPlNamespace(long plNamespace) {
        this.plNamespace = plNamespace;
    }

    public String getPlTitle() {
        return plTitle;
    }

    public void setPlTitle(String plTitle) {
        this.plTitle = plTitle;
    }
}
