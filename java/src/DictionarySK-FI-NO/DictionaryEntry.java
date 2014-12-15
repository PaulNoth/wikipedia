package vi_dictionary;


public class DictionaryEntry {
    
    private int ID;
    private LanguageLink SK;
    private LanguageLink FI;
    private LanguageLink NO;
    
    public int GetID()
    {
        return ID;
    }
    public void SetID(int value)
    {
        ID = value;
    }
    
    public LanguageLink GetSKLang()
    {
        return SK;
    }
    public void SetSKLang(LanguageLink value)
    {
        SK = value;
    }
    
    public LanguageLink GetFILang()
    {
        return FI;
    }
    public void SetFILang(LanguageLink value)
    {
        FI = value;
    }

    public LanguageLink GetNOLang()
    {
        return NO;
    }
    public void SetNOLang(LanguageLink value)
    {
        NO = value;
    }
}
