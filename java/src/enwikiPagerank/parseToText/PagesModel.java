package enwikiPagerank.parseToText;
/**
 * Created by Jozef on 20.10.2014.
 */
public class PagesModel {
    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public long getPageNameSpace() {
        return pageNameSpace;
    }

    public void setPageNameSpace(long poageNameSpace) {
        this.pageNameSpace = poageNameSpace;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public int getPageIsRedirect() {
        return pageIsRedirect;
    }

    public void setPageIsRedirect(int pageIsRedirect) {
        this.pageIsRedirect = pageIsRedirect;
    }

    private int pageId;
    private long pageNameSpace;
    private String pageTitle;
    private int pageIsRedirect;

}