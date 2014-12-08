wikipedia
=========

Parsing of useful data from Wikipedia, DBPedia and Freebase 

Zdrojove kody su pisane v programovacom jazyku C#. Otvorit sa daju v lubovolnom prostredy podporujucom tneto jazyk odporucam
vsak pouzit visual studio kedze su tam pre neho vytvorene solution files (jednoduchsia praca -> netreba robit import do nejakeho ineho IDE)
Odporucam precitat si dokumentaciu (http://vi.ikt.ui.sav.sk/User:xmeszarosm1?view=home) kde je presnejsie popis riesenia tu bude len postup spustenia.
Pred spustenim je potrebne prebehnut cele data prikazom grep -A 7 "{{PersonData" aby sme vyparsovali potrebne data na spracovanie
Nasledne je potrebne spustit program WikiParser 
Dolezite je v kode mu zadat cesty k suborom s datami. Vystupom su dva subory s nazvom good a bad.
V good su data ktore spr8vne presli parsovacim procesom a prepisali sa na pozadovany format ktory moze byt dalej spracovany. 
V bad su data ktore neboli spracovane dovodom je to ze mali specificky format datumu ktory by bolo velmi zdlhave osetrit pre kazdy typ.
Potom uz len staci spustit program CanTheyMeet kde je taktiez potrebne zadat SPRAVNU CESTU K DATA (konkretne subor good z vysptupu programu WikiParser)
Tento program ma jednoduche GUI kde po zadani mien najde informacie o narodeni a zisti ci sa osobo mohli stretnut. 
Pre zaujmavost odporucam pozriet metodu canTheyMeet() ktora celkom pekne riesi vsetky mozne spojenia vyplnenia datumov.
