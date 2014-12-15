package vi_dictionary;

import java.util.HashMap;
import java.util.Map;

public class Statistics 
{
    /*
    Metoda vypocia statistiky na zaklade slovniku nachadzajuceho sa v pamati
    */
    public void CalculateAllStats(HashMap<Integer,DictionaryEntry> Dictionary)
    {
        int PocetChybaSK=0;
        int PocetChybaFI=0;
        int PocetChybaNO=0;
        int PocetZaznamovVsetko=0;
        int PocetSpolu=0;
        
        if(Dictionary == null)
        {
            System.out.println("Je potrebné najprv vygenerovať záznamy");
            return;
        }
        
        
        for(Map.Entry<Integer, DictionaryEntry> entry : Dictionary.entrySet())
        {
            if(entry.getValue().GetNOLang() != null && entry.getValue().GetFILang()!=null && entry.getValue().GetSKLang()!=null)
            {
                PocetZaznamovVsetko++;
            }
            
            if(entry.getValue().GetNOLang() == null)
            {
                PocetChybaNO++;
            }
            
            if(entry.getValue().GetFILang() == null)
            {
                PocetChybaFI++;
            }
            
            if(entry.getValue().GetSKLang() == null)
            {
                PocetChybaSK++;
            }
            
            PocetSpolu++;    
        }
        
        System.out.println("Počet vyparsovaných záznamov: " + PocetSpolu);
        System.out.println("Počet záznamov s kompletným prekladom: " + (float)PocetZaznamovVsetko/(float)PocetSpolu*100 + " %");
        System.out.println("Počet záznamov s chýbajucím prekladom do jazyka SK: "+ (float)PocetChybaSK/(float)PocetSpolu*100 + " %");
        System.out.println("Počet záznamov s chýbajucím prekladom do jazyka FI: "+ (float)PocetChybaFI/(float)PocetSpolu*100 + " %");
        System.out.println("Počet záznamov s chýbajucím prekladom do jazyka NO: "+ (float)PocetChybaNO/(float)PocetSpolu*100 + " %");
                            
        
    }
}
