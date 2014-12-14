using System;
using System.Text;

namespace WikiParser
{
    /// <summary>
    /// PageInfo class
    /// </summary>
    public class PageInfo
    {
        public string title { get; set; }
        public string anchor { get; set; }
        public string shortDescription { get; set; }
        public string longDescription { get; set; }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="title"></param>
        /// <param name="shortDescription"></param>
        /// <param name="longDescription"></param>
        public PageInfo(string title, string shortDescription, string longDescription)
        {
            this.title = title;
            anchor = "http://sk.wikipedia.org/wiki/" + title.Replace(" ", "_");
            this.shortDescription = shortDescription;
            this.longDescription = longDescription;
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="title"></param>
        /// <param name="anchor"></param>
        /// <param name="shortDescription"></param>
        /// <param name="longDescription"></param>
        public PageInfo(string title, string anchor, string shortDescription, string longDescription)
        {
            this.title = title;
            this.anchor = anchor;
            this.shortDescription = shortDescription;
            this.longDescription = longDescription;
        }

        /// <summary>
        /// Export instance of PageInfo in XML form
        /// </summary>
        /// <returns></returns>
        public string ExportToString()
        {
            StringBuilder sb = new StringBuilder();

            sb.AppendFormat("<page>{0}", Environment.NewLine);
            sb.AppendFormat("<title>{0}</title>{1}", title, Environment.NewLine);
            sb.AppendFormat("<anchor>{0}</anchor>{1}", anchor, Environment.NewLine);
            sb.AppendFormat("<shortDescription>{0}</shortDescription>{1}", shortDescription, Environment.NewLine);
            sb.AppendFormat("<longDescription>{0}</longDescription>{1}", longDescription, Environment.NewLine);
            sb.AppendFormat("</page>{0}", Environment.NewLine);

            return sb.ToString();
        }

        /// <summary>
        /// Export instance of PageInfo in txt form
        /// </summary>
        /// <returns></returns>
        public string ExportTo()
        {
            StringBuilder sb = new StringBuilder();

            sb.AppendFormat("Title:{0}{1}{0}", Environment.NewLine, title);
            sb.AppendFormat("{0}Anchor:{0}{1}{0}", Environment.NewLine, anchor);
            sb.AppendFormat("{0}Short description:{0}{1}{0}", Environment.NewLine, shortDescription);
            sb.AppendFormat("{0}Long description:{0}{1}{0}", Environment.NewLine, longDescription);

            return sb.ToString();
        }

        /// <summary>
        /// Overriden ToString() methode for displaying PageInfo instance in ListBox 
        /// </summary>
        /// <returns></returns>
        public override string ToString()
        {
            return title;
        }
    }
}
