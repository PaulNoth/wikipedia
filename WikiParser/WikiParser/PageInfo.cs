using System;
using System.Text;

namespace WikiParser
{
    public class PageInfo
    {
        private string title;
        private string anchor;
        private string description;

        public PageInfo(string title, string description)
        {
            this.title = title;
            this.anchor = "http://sk.wikipedia.org/wiki/" + title.Replace(" ", "_");
            this.description = description;
        }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();

            sb.AppendFormat("<page>{0}", Environment.NewLine);
            sb.AppendFormat("<title>{0}</title>{1}", title, Environment.NewLine);
            sb.AppendFormat("<anchor>{0}</anchor>{1}", anchor, Environment.NewLine);
            sb.AppendFormat("<description>{0}</description>{1}", description, Environment.NewLine);
            sb.AppendFormat("</page>{0}", Environment.NewLine);

            return sb.ToString();
        }
    }
}
