using System.Diagnostics;
using System.Linq;
using System.Text.RegularExpressions;
using NamedEntityExtractorSK.Data.RegexStaticClasses;

namespace NamedEntityExtractorSK.Data
{
	/// <summary>
	/// Data class for infobox
	/// </summary>
	[DebuggerDisplay("Content = {Content}")]
	public class Infobox : KnowlegeData
	{
		#region Methods

		public override void SetRegexAttributes()
		{
			var name = this.Content.Split(new char[] { '|' }).FirstOrDefault();
			var type = InfoboxRegex.Types.FirstOrDefault(x => Regex.Match(name, x).Success);
			
			if(type != null)
			{
				this.Attributes = InfoboxRegex.TypeAttributes[type];
			}
			else
			{
				this.Items.Clear();
				//this.Attributes = InfoboxRegex.TypeAttributes.First().Value;
			}
		}

		#endregion
	}
}
