using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Xml;
using System.Xml.Linq;

namespace WikiParser
{
    /// <summary>
    /// Parser class
    /// </summary>
    public class Parser
    {
        /// <summary>
        /// Load, parse and show raw XML wiki dump file
        /// </summary>
        /// <param name="rawInput"></param>
        /// <returns></returns>
        public List<DisambiguationPageInfo> GetDisambiguationPagesFromWikiDump(string rawInput)
        {
            List<DisambiguationPageInfo> disambiguationPages = new List<DisambiguationPageInfo>();

            FindDisambiguationPages(rawInput, disambiguationPages);
            FillDisambiguationPages(rawInput, disambiguationPages);
            RemoveDuplicatePages(disambiguationPages);
            disambiguationPages = disambiguationPages.OrderBy(o => o.title).ToList();

            return disambiguationPages;
        }

        /// <summary>
        /// Load and show parsed XML file
        /// </summary>
        /// <param name="parsedInput"></param>
        /// <returns></returns>
        public List<DisambiguationPageInfo> GetDisambiguationPagesFromParsedWikiDump(string parsedInput)
        {
            List<DisambiguationPageInfo> disambiguationPages = new List<DisambiguationPageInfo>();
            LoadDisambiguationPages(parsedInput, disambiguationPages);

            return disambiguationPages;
        }

        /// <summary>
        /// Find all disambiguation pages in raw wiki dump
        /// </summary>
        /// <param name="rawInput"></param>
        /// <param name="disambiguationPages"></param>
        private void FindDisambiguationPages(string rawInput, List<DisambiguationPageInfo> disambiguationPages)
        {
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
                            if (el.Value.Contains("(rozlišovacia stránka)"))
                            {
                                while (reader.Read())
                                {
                                    if (reader.NodeType == XmlNodeType.Element)
                                    {
                                        if (reader.Name == "text")
                                        {
                                            XElement ell = XNode.ReadFrom(reader) as XElement;

                                            var lineMatches = Regex.Matches(ell.Value, @"\*(.*?)\[\[(.*?)\]\]");
                                            List<Tuple<string, string>> pagesTitlesDescs = new List<Tuple<string, string>>();
                                            foreach (Match lineMatch in lineMatches)
                                            {
                                                //string desc = lineMatch.Groups[1].ToString().Split(',')[0];
                                                string desc = lineMatch.Value.Replace("[[", "").Replace("]]", "");
                                                string title = lineMatch.Groups[2].ToString();
                                                pagesTitlesDescs.Add(new Tuple<string, string>(title, desc));

                                                if (title.Contains("|"))
                                                {
                                                    string[] splitedTitles = title.Split('|');
                                                    foreach (string ss in splitedTitles)
                                                    {
                                                        pagesTitlesDescs.Add(new Tuple<string, string>(ss, desc));
                                                    }
                                                }

                                            }
                                            //var matches = Regex.Matches(ell.Value, @"\[\[(.*?)\]\]");                                                                                        
                                            DisambiguationPageInfo dpi = new DisambiguationPageInfo(el.Value, pagesTitlesDescs);
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

        /// <summary>
        /// Find all pages for found disambiguation pages in raw wiki dump
        /// </summary>
        /// <param name="rawInput"></param>
        /// <param name="disambiguationPages"></param>
        private void FillDisambiguationPages(string rawInput, List<DisambiguationPageInfo> disambiguationPages)
        {
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

                            foreach (DisambiguationPageInfo disambiguationPageInfo in disambiguationPages)
                            {
                                foreach (Tuple<string, string> pageTitleDesc in disambiguationPageInfo.pagesTitlesDescs)
                                {
                                    if (el.Value.ToLower() == pageTitleDesc.Item1.ToLower())
                                    {
                                        while (reader.Read())
                                        {
                                            if (reader.NodeType == XmlNodeType.Element)
                                            {
                                                if (reader.Name == "text")
                                                {
                                                    XElement ell = XNode.ReadFrom(reader) as XElement;
                                                    string longDescription = "";
                                                    string shortDescription = "";
                                                    if (ell.Value.Length >= 4)
                                                        longDescription = GetPageLongDescription(ell.Value, el.Value);
                                                    if (pageTitleDesc.Item2.Length >= 4)
                                                        shortDescription = pageTitleDesc.Item2;

                                                    PageInfo pageInfo = new PageInfo(el.Value, shortDescription, longDescription);
                                                    
                                                    disambiguationPageInfo.pages.Add(pageInfo);
                                                    disambiguationPageInfo.pagesTitlesDescs.Remove(pageTitleDesc);
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

                foreach (DisambiguationPageInfo disambiguationPageInfo in disambiguationPages)
                {
                    for (; 0 < disambiguationPageInfo.pagesTitlesDescs.Count; )
                    {
                        PageInfo pageInfo;
                        pageInfo = new PageInfo(disambiguationPageInfo.pagesTitlesDescs[0].Item1, disambiguationPageInfo.pagesTitlesDescs[0].Item2, "");

                        disambiguationPageInfo.pages.Add(pageInfo);
                        disambiguationPageInfo.pagesTitlesDescs.Remove(disambiguationPageInfo.pagesTitlesDescs[0]);
                    }
                }
            }
        }

        /// <summary>
        /// Load and show parsed XML file
        /// </summary>
        /// <param name="parsedInput"></param>
        /// <param name="disambiguationPages"></param>
        private void LoadDisambiguationPages(string parsedInput, List<DisambiguationPageInfo> disambiguationPages)
        {
            using (XmlReader reader = XmlReader.Create(parsedInput))
            {
                reader.MoveToContent();
                while (reader.Read())
                {
                    if (reader.NodeType == XmlNodeType.Element)
                    {
                        if (reader.Name == "dspage")
                        {
                            Label:
                            DisambiguationPageInfo disambiguationPage = new DisambiguationPageInfo();
                            while (reader.Read())
                            {
                                if (reader.NodeType == XmlNodeType.Element)
                                {
                                    if (reader.Name == "title")
                                    {
                                        XElement el = XNode.ReadFrom(reader) as XElement;
                                        string title = el.Value;
                                        disambiguationPage.title = title;
                                    }
                                    else if (reader.Name == "page")
                                    {
                                        PageInfo page;
                                        string title = "";
                                        string anchor = "";
                                        string shortDescription = "";
                                        string longDescription = "";

                                        while (reader.Read())
                                        {
                                            if (reader.NodeType == XmlNodeType.Element)
                                            {
                                                if (reader.Name == "title")
                                                {
                                                    XElement el = XNode.ReadFrom(reader) as XElement;
                                                    title = el.Value;
                                                }
                                                else if (reader.Name == "anchor")
                                                {
                                                    XElement el = XNode.ReadFrom(reader) as XElement;
                                                    anchor = el.Value;
                                                }
                                                else if (reader.Name == "shortDescription")
                                                {
                                                    XElement el = XNode.ReadFrom(reader) as XElement;
                                                    shortDescription = el.Value;
                                                }
                                                else if (reader.Name == "longDescription")
                                                {
                                                    XElement el = XNode.ReadFrom(reader) as XElement;
                                                    longDescription = el.Value;
                                                    page = new PageInfo(title, anchor, shortDescription, longDescription);
                                                    disambiguationPage.pages.Add(page);
                                                }
                                                else if (reader.Name == "dspage")
                                                {
                                                    disambiguationPages.Add(disambiguationPage);
                                                    goto Label;
                                                }
                                            }
                                        }
                                        disambiguationPages.Add(disambiguationPage);
                                    }
                                }
                            }
                            
                        }
                    }
                }
            }
        }

        /// <summary>
        /// Remove pages that are duplicates
        /// </summary>
        /// <param name="disambiguationPages"></param>
        public void RemoveDuplicatePages(List<DisambiguationPageInfo> disambiguationPages)
        {
            foreach (DisambiguationPageInfo disambiguationPage in disambiguationPages)
            {
                List<PageInfo> distinctPages = new List<PageInfo>(disambiguationPage.pages);
                foreach (PageInfo page1 in disambiguationPage.pages)
                {
                    if (page1.longDescription.Length > 0)
                    {
                        foreach (PageInfo page2 in disambiguationPage.pages)
                        {
                            if (page2.longDescription.Length == 0 && page1.shortDescription == page2.shortDescription)
                            {
                                distinctPages.Remove(page2);
                            }
                        }
                    }
                }
                disambiguationPage.pages = distinctPages;
            }
        }

        /// <summary>
        /// Remove metadata from raw long description
        /// </summary>
        /// <param name="text"></param>
        /// <param name="title"></param>
        /// <returns></returns>
        public string GetPageLongDescription(string text, string title)
        {
            string desc = "";

            int indexStart = text.ToLower().IndexOf(String.Format("'''{0}'''", title.ToLower()));
            if (indexStart >= 0)
            {
                desc = text.Substring(indexStart);

                int indexEndChapter = desc.IndexOf("\n\n");
                if (indexEndChapter > 0)
                {
                    desc = desc.Substring(0, indexEndChapter);                   
                }

                if (desc.Length > 0)
                {
                    while (true)
                    {
                        int startElement = desc.IndexOf('<');
                        int endElement = desc.IndexOf('>');

                        if (startElement < endElement && startElement > 0)
                        {
                            desc = desc.Remove(startElement, endElement - startElement);
                        }
                        else
                        {
                            break;
                        }
                    }

                    while (true)
                    {
                        int startElement = desc.IndexOf("({{");
                        int endElement = desc.IndexOf("}})");

                        if (startElement < endElement && startElement > 0)
                        {
                            desc = desc.Remove(startElement, endElement - startElement + 3);
                        }
                        else
                        {
                            break;
                        }
                    }

                    while (true)
                    {
                        int startElement = desc.IndexOf("[[");
                        int middleElement = desc.IndexOf('|');
                        int endElement = desc.IndexOf("]]");

                        if (startElement < middleElement && middleElement < endElement && startElement > 0)
                        {
                            desc = desc.Remove(endElement, 2);
                            desc = desc.Remove(startElement, middleElement - startElement + 1);
                        }
                        else if (startElement < middleElement && middleElement > endElement && startElement > 0)
                        {
                            desc = desc.Remove(endElement, 2).Remove(startElement, 2);
                        }
                        else
                        {
                            break;
                        }
                    }

                    desc = desc.Replace("'''", "").Replace("[[", "").Replace("]]", "").Replace("<", "").Replace(">", "");
                }
            }


            return desc;
        }
    }   
}
