=======================

Zadanie projektu:
Každý článok na Wikipédii sa zaradzuje do jednej, či viacerých kategórií. Kategórie majú článok čo najvýstižnejšie opisovať a používateľ by mal intuitívne nájsť hľadný článok už len podľa kategórie.

Mojou úlohou bude vytvorenie algoritmu pre extrahovanie kategórií z nemeckej Wikipédie (nemecká Wiki preto, lebo je jednou z najväčších zbierok článkov a teda poskytuje jeden z najväčších priestorov) a ich následné porovnanie s SQL dumpom. Pre vstupné parsovanie zvolím XML dáta všetkých článkov, ktoré sú prístupné na voľné stiahnutie (odkaz nižšie).

Vstupné dáta do parsera:

XML súbor so všetkými článkami, z ktorého sa budú extrahovať kategórie
SQL súbor s kategóriami
SQL súbor, kde sú kategórie namapované na ID článkov

Výstupné dáta z parsera:

XML súbor s kategóriami a početnosťou zo vstupného XML
XML súbor s kategóriami a početnosťou zo vstupného SQL
TXT súbor s článkami a ich ID
TXT súbor s kategóriami a ID článkov, ktorým patria

Aplikácia
Pred použitím samotnej aplikácie sa načítajú a spracujú výstupné dáta z parsera (celkovo 4 súbory). Každý súbor sa spracúva v samostatnom Threade. Dva vstupné XML súbory sa spracúvajú v špeciálne upravenom XML parseri, ktorý využíva aj SAX, aj DOM. Načítané dáta sú uložené v pamäti RAM v HashMap štruktúre. Zvyšné dva TXT súbory sa spracúvajú cez indexovací nástroj Lucene, ktorý následne umožňuje full-textové vyhľadávanie.

Spracovanie dvoch asi 100MB textových súborov cez StandardAnalyzer trvalo necelých 120 sekúnd. Ak sa namiesto toho použil SimpleAnalyzer, čas sa skráti asi na 80 sekúnd. No SimpleAnalyzer má svoje nevýhody, preto radšej používam ten klasický analyzér (aj keď to trvá o niečo dlhšie). Textový súbor sa načíta a z každého riadka sa vytvorí samostatný Document, ktorý sa zaindexuje.

Aplikácia dokáže dva druhy vecí - ak sa zadá do textového poľa kategória alebo názov článku (aj čiastočný, ale musí byť celé slovo), zobrazí sa vo výstupe (max) 10 nájdených článkov s prislúchajúcimi kategóriami a (max) 10 kategórií v článkoch. Pochopiteľne, ak sa zadaný výraz nenachádza medzi článkami, hľadá sa potom iba medzi kategóriami a opačne. Druhou funkcionalitou je ComboBox s preddefinovanými štatistickými dátami. Teda sa dá napríklad zobraziť kategória s max výskytmi v článkoch, alebo iba porovnať počty kategórii z XML a SQL navzájom, či sa rovnajú. 

Inštalácia a spustenie:
Nie je potrebná inštalácia, stačí vo vývojovom prostredí spustiť metódu Main. V  priložených zdrojákoch sú dve: jedna v balíčku parser (Main.java), ten slúži na spustenie parsera. Cesty sa nastavujú priamo v kóde, defaultne sa berie priečinok "data" umiestnený na rovnakej úrovni ako priečinky libs, bin, src atď.

Druhá Main metóda je v balíčku GUI a slúži na spustenie GUI okna. Pri spustení je treba mať spávne umiestnené 4 vstupné súbory, tie musia byť v nedávno spomínanom priečinku "data". Sú potrebné aj tri základné .jar Lucene knižnice (core, analyzer a queryparser) 

Zdroje dát:
http://dumps.wikimedia.org/dewiki/20140918/dewiki-20140918-pages-articles-multistream.xml.bz2

http://dumps.wikimedia.org/dewiki/latest/dewiki-latest-category.sql.gz

http://dumps.wikimedia.org/dewiki/latest/dewiki-latest-categorylinks.sql.gz

Github:
https://github.com/MartinLondak/vi-wikipedia-categories
