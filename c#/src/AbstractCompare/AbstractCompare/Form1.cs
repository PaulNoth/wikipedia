using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Xml;
using System.Text.RegularExpressions;
using System.IO;
using Lucene.Net.Analysis;
using Lucene.Net.Analysis.Standard;
using Lucene.Net.Documents;
using Lucene.Net.Index;
using Lucene.Net.QueryParsers;
using Lucene.Net.Search;
using Lucene.Net.Store;
using Directory = Lucene.Net.Store.Directory;
using Version = Lucene.Net.Util.Version;


namespace AbstractCompare
{
    public partial class Form1 : Form
    {
        string cestaSuborWiki = "";
        string cestaSuborDBPedia = "";

        string abstraktWiki = "";
        string abstraktDBpedia = "";

        int pocetSlovWiki = 0;
        int pocetSlovDB = 0;

        double podobnostAbstraktov = 0;

        Dictionary<string, int> pocetnostSlovDB;
        Dictionary<string, int> pocetnostSlovWiki;

        Directory directoryWiki;
        Analyzer analyzerWiki;

        Directory directoryDBPedia;
        Analyzer analyzerDBPedia;

        bool MamIndexWiki = false;
        bool MamIndexDBPedia = false;
        
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {                    
            
        }

        //----------------------------- Wikipedia ---------------------------------------------------//

        private void button8_Click(object sender, EventArgs e) //vytvor subor - zjednodusenie velkeho suboru pre wikipediu
        {
            cestaSuborWiki = textBox1.Text;
            string nadpis = "";
            string abstraktWikiSubor = "";

            bool zapisalTitle = false;
            bool zapisalAbs = false;
            bool zapisalSom = false;

            XmlTextReader citam = new XmlTextReader(cestaSuborWiki);

            using (System.IO.StreamWriter subor = new System.IO.StreamWriter(@"d:\Vyhladavanie info\wiki-abstract-vycuc.txt"))
            {
                while (citam.Read())
                {
                    if (citam.IsStartElement())
                    {
                        switch (citam.Name)
                        {
                            case "doc":
                                zapisalSom = false;
                                break;

                            case "title":
                                nadpis = citam.ReadString();
                                nadpis = nadpis.Remove(0, 11);
                                nadpis = nadpis.ToLower();
                                zapisalTitle = true;
                                break;

                            case "abstract":
                                abstraktWikiSubor = citam.ReadString();
                                zapisalAbs = true;
                                break;

                            case "links":
                                if (!zapisalSom)
                                {
                                    MessageBox.Show("Zmena štruktúry XML súboru. Oprav zdrojový kód!"); //ochrana pred zmenou struktury XML suboru
                                    break;
                                }
                                break;
                        }
                    }

                    if (zapisalTitle && zapisalAbs)
                    {
                        subor.WriteLine(nadpis + ";|?" + abstraktWikiSubor);
                        zapisalSom = true;
                        zapisalAbs = false;
                        zapisalTitle = false;
                    }
                }
            }
        }
        
        private void button1_Click(object sender, EventArgs e) // Zobraz abstrakt pre wikipediu
        {
            if (textBox1.Text.Equals(""))
            {
                MessageBox.Show("Zadajte cestu k suboru!");
            }
            else
            {
                if (textBox3.Text.Equals(""))
                {
                    MessageBox.Show("Vyplňte políčko: Zadajte hľadaný abstrakt!");
                }
                else
                {
                    //ak nemam este vyskladany index z mojho suboru
                    if (!MamIndexWiki)
                    {
                        directoryWiki = FSDirectory.Open(new DirectoryInfo(Environment.CurrentDirectory + "\\WikiIndex"));
                        analyzerWiki = new StandardAnalyzer(Version.LUCENE_29);
                        IndexWriter writer = new IndexWriter(directoryWiki, analyzerWiki, true, IndexWriter.MaxFieldLength.UNLIMITED);

                        string riadok;
                        cestaSuborWiki = textBox1.Text;

                        System.IO.StreamReader subor = new System.IO.StreamReader(cestaSuborWiki);

                        while ((riadok = subor.ReadLine()) != null)
                        {
                            string[] poleAbstrakt = riadok.Split(new string[] { ";|?" }, StringSplitOptions.None);

                            //ideme indexovat kazdy jeden riadok ktory sme si predtym podelili na title a abstrakt
                            Document doc = new Document();

                            doc.Add(new Field("Nadpis", poleAbstrakt[0], Field.Store.YES, Field.Index.ANALYZED));
                            doc.Add(new Field("Abstrakt", poleAbstrakt[1], Field.Store.YES, Field.Index.NO));

                            writer.AddDocument(doc);

                        }
                        //zavrieme a aktualizujeme zapisovac a ostatne srandy
                        MamIndexWiki = true;
                        writer.Optimize();
                        //writer.Commit();
                        writer.Close();
                        MessageBox.Show("Indexovanie súboru prebehlo úspešne!");
                    }

                    if (MamIndexWiki)
                    {
                        //otvorenie directory na citanie indexov a definovanie hladaca 
                        IndexReader indexReader = IndexReader.Open(directoryWiki, true);
                        Searcher indexSearch = new IndexSearcher(indexReader);

                        //vytvorime samotny vyhladavac, definujeme verziu a pole v ktorom chceme hladat, odovzdame dopyt zadany na vstupe 
                        QueryParser queryParser = new QueryParser(Version.LUCENE_29, "Nadpis", analyzerWiki);
                        Query query = queryParser.Parse(textBox3.Text);

                        //samotne hladanie a naplnenie struktury pre vysledky - chceme prvych 20 vysledkov
                        TopDocs resultDocs = indexSearch.Search(query, 20);

                        //najdene vysledky hladania
                        var hits = resultDocs.scoreDocs;

                        richTextBox1.Text = "";
                        abstraktWiki = "";

                        textBox4.Text = resultDocs.totalHits.ToString();

                        if (resultDocs.totalHits > 0)
                        {
                            foreach (var hit in hits)
                            {
                                var documentFromSearcher = indexSearch.Doc(hit.doc);

                                richTextBox1.Text += documentFromSearcher.Get("Nadpis") + ": " + documentFromSearcher.Get("Abstrakt") + "\n";

                                abstraktWiki += documentFromSearcher.Get("Abstrakt") + " ";
                            }
                        }
                        else
                        {
                            richTextBox1.Text = "Nenasla sa ziadna zhoda";
                        }
                    }
                }
            }
        }

        private void button6_Click(object sender, EventArgs e) //Spocitaj pocet vyskytov slov pre wikipediu
        {
            abstraktWiki = Regex.Replace(abstraktWiki, "[^0-9a-zA-Z]+", " ");
            abstraktWiki = abstraktWiki.ToLower();
            
            List<string> zoznamSlovWiki = abstraktWiki.Split(' ').ToList();
            pocetnostSlovWiki = new Dictionary<string, int>();
            
            foreach (string slovo in zoznamSlovWiki)
            {
                if (slovo.Length >= 1)
                {
                    //pocetSlovWiki++;
                    if (pocetnostSlovWiki.ContainsKey(slovo))
                    {
                        pocetnostSlovWiki[slovo]++;
                    }
                    else
                    {
                        pocetnostSlovWiki[slovo] = 1;
                    }
                }
            }

            pocetSlovWiki = zoznamSlovWiki.Count;
            richTextBox3.Text = "";

            var triedenaPocetnostSlovWiki = (from vstup in pocetnostSlovWiki orderby vstup.Value descending select vstup).ToDictionary(pair => pair.Key, pair => pair.Value);

            foreach (KeyValuePair<string, int> parik in triedenaPocetnostSlovWiki)
            {
                richTextBox3.Text += parik.Key + "\t\t" + parik.Value + "\t" + "Vyskyt: " + Math.Round((double)parik.Value / pocetSlovWiki, 3) + "\n";
            }
        }

        private void button4_Click(object sender, EventArgs e) // najdi cestu pre wikipediu
        {
            openFileDialog1.ShowDialog();
            textBox1.Text = openFileDialog1.FileName;
        }

        //*****************************************************************************************//
        //----------------------------- DBPedia ---------------------------------------------------//

        private void button9_Click(object sender, EventArgs e) //vytvor zjednoduseny subor pre dbpediu
        {
            cestaSuborDBPedia = textBox2.Text;

            string riadok;

            System.IO.StreamReader citajSubor = new System.IO.StreamReader(cestaSuborDBPedia);

            using (System.IO.StreamWriter zapisSubor = new System.IO.StreamWriter(@"d:\Vyhladavanie info\dbpedia-abstract-vycuc.txt"))
            {
                while ((riadok = citajSubor.ReadLine()) != null)
                {
                    string[] poleAbstrakt = riadok.Split(new string[] { "<http://db" }, StringSplitOptions.None);

                    if (poleAbstrakt.Length > 2)
                    {
                        //Unit test pre testovanie struktury dokumentu
                        string unitTest = poleAbstrakt[1].Substring(0, 19);

                        if (!unitTest.Equals("pedia.org/resource/"))
                        {
                            MessageBox.Show("Štruktúra nie je rovnaká! Oprav zdrojový kód!");
                            break;
                        }
                        //----------------------------------------------
                        string nadpis = poleAbstrakt[1].Remove(0, 19);
                        nadpis = nadpis.Remove(nadpis.Length - 2);
                        nadpis = nadpis.Replace("_", " ").ToLower();

                        poleAbstrakt[2] = poleAbstrakt[2].Remove(poleAbstrakt[2].Length - 6);
                        poleAbstrakt[2] = poleAbstrakt[2].Remove(0, 30);

                        //ideme zapisovat vytvorene prvky do noveho suboru
                        zapisSubor.WriteLine(nadpis + ";|?" + poleAbstrakt[2]);
                    }
                }
                zapisSubor.Close();
            }
            citajSubor.Close();
        }
        
        private void button2_Click(object sender, EventArgs e) //zobraz abstrakt pre dbpediu
        {
            if (textBox2.Text.Equals(""))
            {
                MessageBox.Show("Zadajte cestu k suboru!");
            }
            else
            {
                if (textBox3.Text.Equals(""))
                {
                    MessageBox.Show("Vyplňte políčko: Zadajte hľadaný abstrakt!");
                }
                else
                {
                    //ak nemam este vyskladany index z mojho suboru
                    if (!MamIndexDBPedia)
                    {
                        directoryDBPedia = FSDirectory.Open(new DirectoryInfo(Environment.CurrentDirectory + "\\DBPediaIndex"));
                        analyzerDBPedia = new StandardAnalyzer(Version.LUCENE_29);
                        IndexWriter writer = new IndexWriter(directoryDBPedia, analyzerDBPedia, true, IndexWriter.MaxFieldLength.UNLIMITED);

                        string riadok;
                        cestaSuborDBPedia = textBox2.Text;

                        System.IO.StreamReader subor = new System.IO.StreamReader(cestaSuborDBPedia);

                        while ((riadok = subor.ReadLine()) != null)
                        {
                            string[] poleAbstrakt = riadok.Split(new string[] { ";|?" }, StringSplitOptions.None);

                            //ideme indexovat kazdy jeden riadok ktory sme si predtym podelili na title a abstrakt
                            Document doc = new Document();

                            doc.Add(new Field("Nadpis", poleAbstrakt[0], Field.Store.YES, Field.Index.ANALYZED));
                            doc.Add(new Field("Abstrakt", poleAbstrakt[1], Field.Store.YES, Field.Index.NO));

                            writer.AddDocument(doc);

                        }
                        //zavrieme a aktualizujeme zapisovac a ostatne srandy
                        MamIndexDBPedia = true;
                        writer.Optimize();
                        //writer.Commit();
                        writer.Close();
                        MessageBox.Show("Indexovanie súboru prebehlo úspešne!");
                    }

                    if (MamIndexDBPedia)
                    {
                        //otvorenie directory na citanie indexov a definovanie hladaca 
                        IndexReader indexReader = IndexReader.Open(directoryDBPedia, true);
                        Searcher indexSearch = new IndexSearcher(indexReader);

                        //vytvorime samotny vyhladavac, definujeme verziu a pole v ktorom chceme hladat, odovzdame dopyt zadany na vstupe 
                        QueryParser queryParser = new QueryParser(Version.LUCENE_29, "Nadpis", analyzerDBPedia);
                        Query query = queryParser.Parse(textBox3.Text);

                        //samotne hladanie a naplnenie struktury pre vysledky
                        TopDocs resultDocs = indexSearch.Search(query, 20);

                        //najdene vysledky hladania
                        var hits = resultDocs.scoreDocs;

                        richTextBox2.Text = "";
                        abstraktDBpedia = "";

                        textBox5.Text = resultDocs.totalHits.ToString();

                        if (resultDocs.totalHits > 0)
                        {
                            foreach (var hit in hits)
                            {
                                var documentFromSearcher = indexSearch.Doc(hit.doc);

                                richTextBox2.Text += documentFromSearcher.Get("Nadpis") + ": " + documentFromSearcher.Get("Abstrakt") + "\n";

                                abstraktDBpedia += documentFromSearcher.Get("Abstrakt") + " ";
                            }
                        }
                        else
                        {
                            richTextBox2.Text = "Nenasla sa ziadna zhoda";
                        }
                    }
                }
            }
        }

        private void button7_Click(object sender, EventArgs e) //Spocitaj vyskyt slov pre DBPediu
        {           
            abstraktDBpedia = Regex.Replace(abstraktDBpedia, "[^0-9a-zA-Z]+", " ");
            abstraktDBpedia = abstraktDBpedia.ToLower();
            
            List<string> zoznamSlovDB = abstraktDBpedia.Split(' ').ToList();
            pocetnostSlovDB = new Dictionary<string, int>();

            foreach (string slovo in zoznamSlovDB)
            {
                if (slovo.Length >= 1)
                {
                    //pocetSlovDB++;
                    if (pocetnostSlovDB.ContainsKey(slovo))
                    {
                        pocetnostSlovDB[slovo]++;
                    }
                    else
                    {
                        pocetnostSlovDB[slovo] = 1;
                    }
                }
            }

            pocetSlovDB = zoznamSlovDB.Count;
            richTextBox4.Text = "";

            var triedenaPocetnostSlovDB = (from vstup in pocetnostSlovDB orderby vstup.Value descending select vstup).ToDictionary(pair => pair.Key, pair => pair.Value);

            foreach (KeyValuePair<string, int> parik in triedenaPocetnostSlovDB)
            {
                richTextBox4.Text += parik.Key + "\t" + parik.Value + "\t" + "Vyskyt: " + Math.Round((double)parik.Value / pocetSlovDB, 3) + "\n";
            }
        }

        private void button5_Click(object sender, EventArgs e) //najdi cestu pre dbpediu
        {
            openFileDialog2.ShowDialog();
            textBox2.Text = openFileDialog2.FileName;
        }

        //*****************************************************************************************//
        //----------------------------- Vysledok ---------------------------------------------------//

        private void button3_Click(object sender, EventArgs e) //tlacica Vysledok
        {
            bool zhoda = false;
            string zhodneSlovo = "";

            richTextBox5.Text = pocetSlovWiki.ToString();
            richTextBox6.Text = pocetSlovDB.ToString();

            if (pocetnostSlovDB == null || pocetnostSlovWiki == null)
            {
                if (pocetnostSlovDB == null)
                {
                    MessageBox.Show("Nenašiel som údaje na spracovanie z DBPedie!");
                }
                else
                {
                    MessageBox.Show("Nenašiel som údaje na spracovanie z Wikipedie!");
                }
            }
            else
            {
                foreach (KeyValuePair<string, int> parik in pocetnostSlovWiki)
                {
                    foreach (KeyValuePair<string, int> parikDB in pocetnostSlovDB)
                    {
                        if (parik.Key.Equals(parikDB.Key))
                        {
                            zhodneSlovo += parik.Key + " ";
                            double pomocna = Math.Abs(Math.Round((double)parik.Value / pocetSlovWiki, 3) - Math.Round((double)parikDB.Value / pocetSlovDB, 3));
                            podobnostAbstraktov += pomocna;
                            zhoda = true;
                        }
                    }
                }
            }

            if (zhoda)
            {
                richTextBox7.Text = zhodneSlovo;
                richTextBox8.Text = podobnostAbstraktov.ToString();
            }
            else
            {
                richTextBox7.Text = "Ziadne slova sa nezhoduju.";
                richTextBox8.Text = "0";
            }
        }

        //*****************************************************************************************//

        private void textBox1_TextChanged(object sender, EventArgs e) //cesta suboru pre wikipediu
        {

        }

        private void textBox2_TextChanged(object sender, EventArgs e) //cesta suboru pre DBPediu
        {

        }

        private void richTextBox5_TextChanged(object sender, EventArgs e)
        {

        }
    }
}
