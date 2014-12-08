wikipedia
=========
Autor: Andrej Stajer
http://vi.ikt.ui.sav.sk/User:Andrej.Stajer?view=home
Veduci: Ondrej.Kassak 

Tema: Vytvorenie kalendára udalostí wikipédie, teda prepojenie dátumov so stránkami na ktorých sa vyskytujú, spolu s jednoduchou informáciou ako pojem s dátumom súvisí


Program bol vyvinuty v prostredi Visual Studio 2013. Na jeho otvorenie staci pridat project WikiCalendar.csproj do vytvoreneho solution vo VS.
Obdobne to plati pre projektTestov. 

vstupne data pre vyuzivanie applikacie  je potrebne upravit. Je potrebne odstanit oznacenie namespace-ov z XML suboru.

Pouzivatelska prirucka:
Pri prvom spusteni je potrebne spracovat zadany XML subor stlacenim tlacidla Load. po ukonceni spracovania sa pod tlacidlom objavi statistika poctov jednotlivych typov udalosti a doplni sa dropdown menu o vsetky najdene datumi. 
Vyber datumu je mozny zadanim textu do textoveho pola kde je defaultne nastaveny datum 19960101. Ak chceme vyuzivat ine sposoby je potrebne tento datum vymazat. Druhy sposob je vyber datumu z dropdown menu. Treti sposob je vyber dna z calendara v pravej casti pouzivatelskeho rozhrania.
Po vybere sa v hlavnom textovom poli objavia vsetkych udalosti spojene s danym datumom.