using System;
using System.Collections.Generic;
using System.Text;

namespace WikiParser
{
    public class DisambiguationPageInfo
    {
        public string title { get; set; }
        public List<string> pagesTitles { get; set; } 
        public List<PageInfo> pages { get; set; }

        public DisambiguationPageInfo(string title, List<string> pagesTitles)
        {
            this.title = title;
            this.pagesTitles = pagesTitles;
            pages = new List<PageInfo>();
        }

        public string ExportToString()
        {
            StringBuilder sb = new StringBuilder();

            sb.AppendFormat("<dspage>{0}", Environment.NewLine);
            sb.AppendFormat("<title>{0}</title>{1}", title, Environment.NewLine);
            foreach (PageInfo pageInfo in pages)
            {
                sb.Append(pageInfo.ExportToString());
            }
            sb.AppendFormat("</dspage>{0}", Environment.NewLine);

            return sb.ToString();
        }
        public override string ToString()
        {
            return title.Split(' ')[0];
        }
    }
}
