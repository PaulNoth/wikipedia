using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using System.Xml;
using System.Xml.Linq;

namespace WikiParser
{
    public partial class Wiki : Form
    {
        //private string vstup = "skwiki-latest-pages-articles.xml";
        private string vstup = "vstup.xml";
        private List<DisambiguationPageInfo> disambiguationPages;
        private int pagesCount = 0;
        private int multipagesCount = 0;

        public Wiki()
        {
            InitializeComponent();
            textBox1.Text = vstup;
            disambiguationPages = new List<DisambiguationPageInfo>();
        }

        private void ExportDisambiguationPages()
        {
            using (StreamWriter sw = new StreamWriter(@"vystup2.xml", false, Encoding.UTF8))
            {
                sw.WriteLine("<dspages>");
                foreach (DisambiguationPageInfo disambiguationPageInfo in disambiguationPages)
                {
                    sw.WriteLine(disambiguationPageInfo.ExportToString());
                }
                sw.WriteLine("</dspages>");
            }
        }

        private void FindDisambiguationPages()
        {
            using (XmlReader reader = XmlReader.Create(vstup))
            {
                reader.MoveToContent();
                int i = 1;
                while (reader.Read())
                {
                    if (reader.NodeType == XmlNodeType.Element)
                    {
                        if (reader.Name == "title")
                        {
                            XElement el = XNode.ReadFrom(reader) as XElement;
                            if (el.Value.Contains("(rozlišovacia stránka)"))
                            {
                                while (reader.Read())
                                {
                                    if (reader.NodeType == XmlNodeType.Element)
                                    {
                                        if (reader.Name == "text")
                                        {
                                            XElement ell = XNode.ReadFrom(reader) as XElement;

                                            var matches = Regex.Matches(ell.Value, @"\[\[(.*?)\]\]");
                                            List<string> pagesTitles = new List<string>();
                                            foreach (Match s in matches)
                                            {
                                                string title = s.Groups[1].ToString();
                                                pagesTitles.Add(title);
                                                if (title.Contains("|"))
                                                {
                                                    string[] splitedTitles = title.Split('|');
                                                    foreach (string ss in splitedTitles)
                                                    {
                                                        pagesTitles.Add(ss);
                                                        pagesCount++;
                                                    }
                                                    multipagesCount++;
                                                    
                                                }
                                                    
                                                pagesCount++;
                                            }
                                            DisambiguationPageInfo dpi = new DisambiguationPageInfo(el.Value, pagesTitles);
                                            disambiguationPages.Add(dpi);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private void FillDisabiguationPages()
        {
            using (XmlReader reader = XmlReader.Create(vstup))
            {
                reader.MoveToContent();
                while (reader.Read())
                {
                    if (reader.NodeType == XmlNodeType.Element)
                    {
                        if (reader.Name == "title")
                        {
                            XElement el = XNode.ReadFrom(reader) as XElement;

                            foreach (DisambiguationPageInfo disambiguationPageInfo in disambiguationPages)
                            {
                                foreach (string pageTitle in disambiguationPageInfo.pagesTitles)
                                {
                                    if (el.Value.ToLower() == pageTitle.ToLower())
                                    {
                                        while (reader.Read())
                                        {
                                            if (reader.NodeType == XmlNodeType.Element)
                                            {
                                                if (reader.Name == "text")
                                                {
                                                    XElement ell = XNode.ReadFrom(reader) as XElement;
                                                    int index = ell.Value.IndexOf("'''");
                                                    PageInfo pageInfo;
                                                    if (index >= 0)
                                                    {
                                                        pageInfo = new PageInfo(el.Value, ell.Value.Length >= (500 + index)
                                                            ? ell.Value.Substring(index, 500)
                                                            : ell.Value.Substring(index));
                                                    }
                                                    else
                                                    {
                                                        pageInfo = new PageInfo(el.Value, ell.Value.Length >= 500
                                                            ? ell.Value.Substring(0, 500)
                                                            : ell.Value);
                                                    }

                                                    disambiguationPageInfo.pages.Add(pageInfo);
                                                    disambiguationPageInfo.pagesTitles.Remove(pageTitle);
                                                    break;
                                                }
                                            }
                                        }
                                        goto Label;
                                    }
                                }
                            }

                            Label:
                            continue;
                        }
                    }
                }
            }
        }

        private void RemoveDuplicatePages()
        {
            int count = 0;
            foreach (DisambiguationPageInfo disambiguationPageInfo in disambiguationPages)
            {
                disambiguationPageInfo.pagesTitles = disambiguationPageInfo.pagesTitles.Distinct().ToList();
                count += disambiguationPageInfo.pagesTitles.Count;
            }
            Console.WriteLine("Rozlisene stranky odfiltrovane- " + count);
        }

        private void BtnBrowseInput_Click(object sender, EventArgs e)
        {
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                vstup = openFileDialog1.FileName;
                textBox1.Text = vstup;
            }
        }

        private void BtnParse_Click(object sender, EventArgs e)
        {
            FindDisambiguationPages();
            RtbProgramInfo.Text += ("Rozlisovacie stranky - " + disambiguationPages.Count + Environment.NewLine);
            RtbProgramInfo.Text += ("Rozlisene stranky - " + pagesCount + Environment.NewLine);
            RtbProgramInfo.Text += ("Rozlisene stranky multi- " + multipagesCount + Environment.NewLine);
            RemoveDuplicatePages();
            RtbProgramInfo.Text += ("Start plnenia rozlisovacich stranok " + DateTime.Now + Environment.NewLine);
            FillDisabiguationPages();
            RtbProgramInfo.Text += ("Koniec plnenia rozlisovacich stranok " + DateTime.Now + Environment.NewLine);
            ShowDisambPages();
        }

        private void BtnExport_Click(object sender, EventArgs e)
        {
            RtbProgramInfo.Text += ("Start vypisu " + DateTime.Now + Environment.NewLine);
            ExportDisambiguationPages();
            RtbProgramInfo.Text += ("Koniec vypisu " + DateTime.Now + Environment.NewLine);
        }

        private void ShowDisambPages()
        {
            LbDisambPages.Items.AddRange(disambiguationPages.ToArray());
            LbDisambPages.SelectedIndex = 0;
        }

        private void LbDisambPages_SelectedIndexChanged(object sender, EventArgs e)
        {
            LbPages.Items.Clear();
            LbPages.Items.AddRange(((DisambiguationPageInfo)LbDisambPages.SelectedItem).pages.ToArray());
            LbPages.SelectedIndex = 0;
        }

        private void LbPages_SelectedIndexChanged(object sender, EventArgs e)
        {
            RtbPagesInfo.Clear();
            RtbPagesInfo.Text += ((PageInfo) (LbPages.SelectedItem)).ExportTo();
        }
    }
}
