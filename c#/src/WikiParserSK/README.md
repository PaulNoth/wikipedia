---------------------------
- Bc. Peter Kiš
- Vyhladávanie informácií
- WikiParserSK
- 2014/2015
---------------------------

-- Inštalácia -------------

Prieèinok c#\src\WikiParserSK\app obsahuje spustite¾nı súbor WikiParser.exe, ktorı staèí spusti.

-- Popis ------------------

Riešenie má formu WinForms okennej aplikácie, ktorá umoòuje pouívate¾ovi spracováva
surové dáta priamo z wikipédie (xml dump), alebo naèíta dáta z u spracovaného xml súboru a pracova
priamo s nimi.
 
V prípade práce so surovımi dátami sa najprv zo zvoleného xml dump-u vyparsujú všetky rozlišovacie
stránky a k nim stránky na ktoré odkazujú. K tımot stránkam sa pri na opätovnom prechode xml dump-om
priradia krátke popisy (short description) tak, ako sa aktuálne zobrazujú na slovenskej wikipédii.

Okrem toho sa k stránkam priradia aj dlhé popisy (long description), ktoré obsahujú úvodnı odsek
z textu tıchto stránok. Takımto spôsobom dostane pouívate¾ ku kadej stránke okrem pôvodného popisu
aj dodatoèné obohacujúce informácie. Po vyparsovaní všetkıch stránok sa jednotlivé rozlišovacie stránky
zobrazia pouívate¾ovi v pripravenom okne, kde si postupnım vıberom môe pouívate¾ pozrie stránky
patriace danej rozlišovacej stránke. K následne vybranej stránke dostane pouívate¾ v preh¾adnom okne
názov a odkaz na wiki-stránku, ako aj krátky a dlhı popis. Pouívate¾ má následne monos takto spracované
dáta vyexportova do xml súboru a v prípade potreby ich neskôr poui pri naèítaní.

V prípade práce s u spracovanımi dátami z predpripraveného xml súboru, sa z vybraného xml súboru naèítajú
iba rozlišovacie stránky a zobrazia sa pouívate¾ovi. Po vıbere nejakej zo zobrazenıch rozlišovacích stránok
sa ku danej stránke naèítajú jej podtránky a po vıbere niektorej z nich sa naèítajú aj náleité informácie
o stránke. Takto sa aplikácia nezahltí pri naèítavaní všetkıch informácií o všetkıch stránkach.

-- Pouitie ---------------

Pre prácu so spracovanımi dátami treba stlaèi tlaèidlo "Load"

Pre prácu so surovımi dátami treba stlaèi tlaèidlo "Parse"

Pre zmenu vstupnıch dát (spracovanıch alebo surovıch) treba stlaèi tlaèidlo "Browse"

Zoznam naèítanıch rozlišovacích stránok sa zobrazí v ¾avom okne.

V strednom okne sa zobrazí zoznam stránok prislúchajúcich vybtanej rozlišovacej stránke.

V pravom okne sa zobrazia informácie o vybranej stránke.

Pri stlaèení tlaèidla "Export" sa spracované surové dáta uloia na vybrané miesto vo formáte .xml
