using System.Collections.Generic;
using System.Xml;
using System.Xml.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using WikiParser;

namespace WikiParserTest
{
    [TestClass]
    public class ParserTest
    {
        [TestMethod]
        public void GetPageLongDescription_test()
        {
            // arrange
            string rawText = @"{{iné významy|röntgen}}

'''Röntgen''', značka '''R''', je zastaraná jednotka [[expozícia|expozície]] [[ionizačné žiarenie|ionizačnému žiareniu]] (značka X). Pomenovaná je po nemeckom fyzikovi [[Wilhelm Röntgen|Wilhelmovi Conradovi Röntgenovi]].

Dnes sa ako jednotka expozície ionizačnému žiareniu používa [[SI]] jednotka [[coulomb na kilogram]].
1 R = 2,58.10&lt;sup&gt;−4&lt;/sup&gt; C.kg&lt;sup&gt;−1&lt;/sup&gt;
Dávka 500 R počas 5 hodín je pre človeka smrteľná.

[[Kategória:Fyzikálne jednotky]]";
            string title = @"Röntgen";
            string expectedDescription = @"Röntgen, značka R, je zastaraná jednotka expozície ionizačnému žiareniu (značka X). Pomenovaná je po nemeckom fyzikovi Wilhelmovi Conradovi Röntgenovi.";
            string gettedDescription;
            Parser parser = new Parser();

            // act
            gettedDescription = parser.GetPageLongDescription(rawText, title);

            // assert
            Assert.AreEqual(expectedDescription, gettedDescription, "Wrong parsed long description!");
        }

        [TestMethod]
        public void GetDisambiguationPagesFromWikiDump_test()
        {
            // arrange
            string rawInput = @"C:\Users\Pleto\Documents\Visual Studio 2013\Projects\WikiParser\WikiParser\bin\Debug\vstup.xml";
            int parsedCount;
            int expectedCount = 5;
            Parser parser = new Parser();

            // act
            parsedCount = parser.GetDisambiguationPagesFromWikiDump(rawInput)[0].pages.Count;

            // assert
            Assert.AreEqual(expectedCount, parsedCount, "Wrong number of parsed pages for Rontgen!");
        }

        [TestMethod]
        public void RemoveDuplicatePages_test()
        {
            // arrange
            int parsedCount;
            int expectedCount = 2;
            Parser parser = new Parser();
            List<DisambiguationPageInfo> disambiguationPages = new List<DisambiguationPageInfo>();
            DisambiguationPageInfo disambPage = new DisambiguationPageInfo();
            disambPage.title = "Muz";
            disambPage.pages.Add(new PageInfo("Peter Kis", "toto som ja", "ano toto som naozaj ja"));
            disambPage.pages.Add(new PageInfo("Peter", "toto som ja", ""));
            disambPage.pages.Add(new PageInfo("Kis", "toto som ja", ""));
            disambPage.pages.Add(new PageInfo("Karol Rastocny", "toto je on", "toto je naozaj on"));
            disambPage.pages.Add(new PageInfo("Karol", "toto je on", ""));
            disambiguationPages.Add(disambPage);

            // act
            parser.RemoveDuplicatePages(disambiguationPages);
            parsedCount = disambiguationPages[0].pages.Count;

            // assert
            Assert.AreEqual(expectedCount, parsedCount, "Wrong number of distinct parsed pages!");
        }

        [TestMethod]
        public void CheckRawDataInputValidity_test()
        {
            // arrange
            string rawInput = @"skwiki-latest-pages-articles.xml";
            string title = "Mars (rozlišovacia stránka)";
            bool expectedResult = true;
            bool returnedResult = false;

            // act
            using (XmlReader reader = XmlReader.Create(rawInput))
            {
                reader.MoveToContent();
                while (reader.Read())
                {
                    if (reader.NodeType == XmlNodeType.Element)
                    {
                        if (reader.Name == "title")
                        {
                            XElement el = XNode.ReadFrom(reader) as XElement;

                            if (el.Value.ToLower() == title.ToLower())
                            {
                                while (reader.Read())
                                {
                                    if (reader.NodeType == XmlNodeType.Element)
                                    {
                                        if (reader.Name == "text")
                                        {
                                            returnedResult = true;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }

            // assert
            Assert.AreEqual(expectedResult, returnedResult, "Wrong XML format!");
        }
    }
}
