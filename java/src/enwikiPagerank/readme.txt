Ahoj,

zdrojaky v tychto adresaroch sa tikaju ratania pageranku pre stranky na anglickej wikipedii.
na stranke: http://vi.ikt.ui.sav.sk/User:Jozef.Marcin?view=home najdes podrobny opis, ako co spustit a ako co funguje.
zdrojaky su okomentovane, tak by sa tomu malo dat porozumiet

unit testy sa nachadzaju v priecinku /root/java/test/enwikiPagerank

ak chces importnut projekt, najlepsie je to takto: 
1. skopci si priecinok root/java/src/enwikiPagerank do nejakeho svojho priecinka na disku
2. do tohto priecinka nakopci este aj priecinok root/java/test/enwikiPagerank/test
3. takze budes mat svoj priecinok /cestaDoPriecinka/enwikiPagerank a vnom budes mat podpriecinky
enwikiPagerank.mapreduce
enwikiPagerank.parseToText
enwikiPagerank.pagerankComputation
test

4. importni do eclipse, alebo ineho IDE
5. v balicku enwikiPagerank.parseToText je hlavna trieda Main.java - ta obsahuje podrobny opis co a ako mas spustit, ale v skratke:

pustenie programu je viac menej jednoduché. Vo vıvojovom prostredí eclipse je ptorebné nastavi jednotlivé atribúty:

1 - konverzia dat z sql do txt
2 - redukcia pagelinks
3 - vytvorenie subdomen
4 - spojenie pages a pagelinks
5 - vytvorenie suboru, ktory neobsahuje linky, ktore v ramci subdomeny ukazuju mino subdomenu
6 - vypocet pageranku
7 - vytvorenie samplu
8 - spustenie unit testu - nad parserom dát a sample vstupmi a vıstupmi
9 - vytvorenie histogramu z hotového vıpoètu pageranku
