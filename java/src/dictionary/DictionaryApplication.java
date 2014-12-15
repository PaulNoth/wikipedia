package dictionary;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.queryparser.classic.ParseException;

/**
 * 
 * Main class.
 * 
 * @author Pidanic
 *
 */
public class DictionaryApplication
{
    public static void main(String[] args) throws IOException, ParseException
    {
        LuceneDbpediaDictionary dictionarySearch;
        System.out.println("Vitajte v aplikácii slovník\n");
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            System.out.println("-> Pre Obohatený slovník zadajte \"1\"");
            System.out.println("-> Pre Jednoduchý slovník zadajte \"2\"");
            System.out.println("-> Pre rozšírené informácie zadajte \"h\"");
            System.out
                    .println("-> Pre koniec zadajte \"q\" (funguje aj pri výbere jazyka)");
            System.out
                    .println("-> Pre vygenerovanie korpusov slovníkov zadajte \"0\" \n  "
                            + "(Upozornenie: Môže trvať veľmi dlhú dobu. Záleží od veľkosti použitých vstupných súborov v priečinku \"data\")");
            System.out.print(">");
            String option = scanner.nextLine();
            if("1".equals(option))
            {
                System.out.println("Slovník sa inicializuje, prosím čakajte");
                System.out.println();
                dictionarySearch = new LuceneEnhancedDictionary();
                System.out.println("Inicializovanie ukončené");
                break;
            }
            else if("2".equals(option))
            {
                System.out.println("Slovník sa inicializuje, prosím čakajte");
                System.out.println();
                dictionarySearch = new LuceneSimpleDictionary();
                System.out.println("Inicializovanie ukončené");
                break;
            }
            else if("q".equalsIgnoreCase(option))
            {
                scanner.close();
                System.exit(0);
            }
            else if("h".equalsIgnoreCase(option))
            {
                showHelp();
            }
            else if("0".equalsIgnoreCase(option))
            {
                DictionaryCorpusCreator dict = new DictionaryCorpusCreator();
                System.out.println("Generujem korpusy slovníkov");
                dict.createEnhancedDictionary();
                dict.createSimpleDictionary();
                System.out.println("Generovanie ukončené");
            }
            else
            {
                System.out.println("Neznáma voľba. Opakujte výber");
            }
        }

        System.out
                .println("Podporované jazyky: Slovenčina (SK), Angličtina (EN), Nemčina (DE), Francúzština (FR)");
        while (true)
        {
            try
            {
                System.out.println("Zadajte preklad z (EN, SK, FR, DE): ");
                System.out.print(">");
                String searchLang = scanner.nextLine().toUpperCase();
                if("q".equalsIgnoreCase(searchLang))
                {
                    break;
                }
                Language from = Language.valueOf(searchLang);
                System.out.println("Zadajte preklad do (EN, SK, FR, DE): ");
                System.out.print(">");
                String toString = scanner.nextLine().toUpperCase();
                if("q".equalsIgnoreCase(toString))
                {
                    break;
                }
                Language to = Language.valueOf(toString);
                System.out.println("Zadajte hľadané slovo: ");
                System.out.print(">");

                String query = scanner.nextLine();
                List<String> result = dictionarySearch.translate(from, to,
                        query);
                System.out.println("\nZobrazujem " + result.size() + " "
                        + getSlovakResult(result.size())
                        + " zoradených podľa najviac vyhovujúceho ");
                for (String s : result)
                {
                    System.out.println(s);
                }
                System.out.println();
                System.out.println();
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("Neznámy jazyk");
            }
        }
        scanner.close();

        dictionarySearch.close();
    }

    private static void showHelp()
    {
        System.out.println("Aplikácia slovník\n\t (c) Pavol Pidanič\n");
        System.out
                .println("Vznikla ako projekt na predmet Vyhľadávanie informácií.\n");
        System.out.println("Aplikácia obsahuje 2 typy prekladových slovníkov:");
        System.out.println("1.) Obohatený slovník");
        System.out
                .println("\tObsahuje preklady pojmov zo 4 jazykov, ale len takých, pre ktoré existuje preklad vo všetkých súčasne.");
        System.out
                .println("\tJe teda možné, že niektoré vyhľadávané slová nebudú preložené, kvôli neexistujúcej väzbe pojmu medzi jazykmi.");
        System.out
                .println("\tNavyše ku každému prekladu aplikácia zobrazí linku na Wikipédiu, ktorá obsahuje ďalšie informácie k danému pojmu.");
        System.out.println("2.) Jednoduchý slovník");
        System.out
                .println("\tPo zadaní ľubovoľného pojmu vo zvolenom jazyku, preloží do druhého jazyka.");
        System.out
                .println("\tAk pre dané slovo neexistuje preklad, je o tom zobrazená informácia.");
        System.out.println();
    }

    private static String getSlovakResult(int n)
    {
        switch (n)
        {
        case 1:
            return "výsledok";
        case 2:
        case 3:
        case 4:
            return "výsledky";
        default:
            return "výsledkov";
        }
    }
}
