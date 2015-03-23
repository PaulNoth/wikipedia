---------------------------
- Bc. Peter Ki�
- Vyhlad�vanie inform�ci�
- WikiParserSK
- 2014/2015
---------------------------

-- In�tal�cia -------------

Prie�inok c#\src\WikiParserSK\app obsahuje spustite�n� s�bor WikiParser.exe, ktor� sta�� spusti�.

-- Popis ------------------

Rie�enie m� formu WinForms okennej aplik�cie, ktor� umo��uje pou��vate�ovi spracov�va�
surov� d�ta priamo z wikip�die (xml dump), alebo na��ta� d�ta z u� spracovan�ho xml s�boru a pracova�
priamo s nimi.
 
V pr�pade pr�ce so surov�mi d�tami sa najprv zo zvolen�ho xml dump-u vyparsuj� v�etky rozli�ovacie
str�nky a k nim str�nky na ktor� odkazuj�. K t�mot str�nkam sa pri na op�tovnom prechode xml dump-om
priradia kr�tke popisy (short description) tak, ako sa aktu�lne zobrazuj� na slovenskej wikip�dii.

Okrem toho sa k str�nkam priradia aj dlh� popisy (long description), ktor� obsahuj� �vodn� odsek
z textu t�chto str�nok. Tak�mto sp�sobom dostane pou��vate� ku ka�dej str�nke okrem p�vodn�ho popisu
aj dodato�n� obohacuj�ce inform�cie. Po vyparsovan� v�etk�ch str�nok sa jednotliv� rozli�ovacie str�nky
zobrazia pou��vate�ovi v pripravenom okne, kde si postupn�m v�berom m��e pou��vate� pozrie� str�nky
patriace danej rozli�ovacej str�nke. K n�sledne vybranej str�nke dostane pou��vate� v preh�adnom okne
n�zov a odkaz na wiki-str�nku, ako aj kr�tky a dlh� popis. Pou��vate� m� n�sledne mo�nos� takto spracovan�
d�ta vyexportova� do xml s�boru a v pr�pade potreby ich nesk�r pou�i� pri na��tan�.

V pr�pade pr�ce s u� spracovan�mi d�tami z predpripraven�ho xml s�boru, sa z vybran�ho xml s�boru na��taj�
iba rozli�ovacie str�nky a zobrazia sa pou��vate�ovi. Po v�bere nejakej zo zobrazen�ch rozli�ovac�ch str�nok
sa ku danej str�nke na��taj� jej podtr�nky a po v�bere niektorej z nich sa na��taj� aj n�le�it� inform�cie
o str�nke. Takto sa aplik�cia nezahlt� pri na��tavan� v�etk�ch inform�ci� o v�etk�ch str�nkach.

-- Pou�itie ---------------

Pre pr�cu so spracovan�mi d�tami treba stla�i� tla�idlo "Load"

Pre pr�cu so surov�mi d�tami treba stla�i� tla�idlo "Parse"

Pre zmenu vstupn�ch d�t (spracovan�ch alebo surov�ch) treba stla�i� tla�idlo "Browse"

Zoznam na��tan�ch rozli�ovac�ch str�nok sa zobraz� v �avom okne.

V strednom okne sa zobraz� zoznam str�nok prisl�chaj�cich vybtanej rozli�ovacej str�nke.

V pravom okne sa zobrazia inform�cie o vybranej str�nke.

Pri stla�en� tla�idla "Export" sa spracovan� surov� d�ta ulo�ia na vybran� miesto vo form�te .xml
