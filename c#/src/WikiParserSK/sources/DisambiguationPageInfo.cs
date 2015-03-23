using System;
using System.Collections.Generic;
using System.Text;

namespace WikiParser
{
    /// <summary>
    /// DisambiguationPageInfo class
    /// </summary>
    public class DisambiguationPageInfo
    {
        public string title { get; set; }
        public List<Tuple<string,string>> pagesTitlesDescs { get; set; } 
        public List<PageInfo> pages { get; set; }

        /// <summary>
        /// Constructor
        /// </summary>
        public DisambiguationPageInfo()
        {
            pages = new List<PageInfo>();
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="title"></param>
        /// <param name="pagesTitlesDescs"></param>
        public DisambiguationPageInfo(string title, List<Tuple<string,string>> pagesTitlesDescs)
        {
            this.title = title;
            this.pagesTitlesDescs = pagesTitlesDescs;
            pages = new List<PageInfo>();
        }

        /// <summary>
        /// Export instance of DisambiguationPageInfo in XML form
        /// </summary>
        /// <returns></returns>
        public string ExportToString()
        {
            var sb = new StringBuilder();

            sb.AppendFormat("<dspage>{0}", Environment.NewLine);
            sb.AppendFormat("<title>{0}</title>{1}", title, Environment.NewLine);
            foreach (var pageInfo in pages)
            {
                sb.Append(pageInfo.ExportToString());
            }
            sb.AppendFormat("</dspage>{0}", Environment.NewLine);

            return sb.ToString();
        }
        
        /// <summary>
        /// Overriden ToString() methode for displaying DisambiguationPageInfo instance in ListBox 
        /// </summary>
        /// <returns></returns>
        public override string ToString()
        {
            return title.Split(' ')[0];
        }
    }
}
