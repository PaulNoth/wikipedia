using System.Diagnostics;
using System.Linq;

namespace NamedEntityExtractorSK.Data
{
	/// <summary>
	/// Data class for citation
	/// </summary>
	[DebuggerDisplay("Content = {Content}")]
	public class Citation : KnowlegeData
	{
		#region Methods

		public override void SetRegexAttributes()
		{
			this.Attributes = new RegexKeyType[] { new RegexKeyType("[Mm]eno[0-9]*", NamedEntityType.Person), 
												   new RegexKeyType("[pP]riezvisko[0-9]*", NamedEntityType.Person), 
												   new RegexKeyType("[vV]ydavate[lľ]", NamedEntityType.Organization), 
												   new RegexKeyType("[mM][i]*esto", NamedEntityType.Location)};
		}

		public void AddFullNames()
		{
			var items = this.Items.Where(x => x.Key.Type == NamedEntityType.Person).ToArray();

			for (int i = 0; i < items.Count(); i += 2)
			{
				if (items.Count() > i+1)
				{

					this.Items.Add(new RegexKeyType("FullName" + i, NamedEntityType.Person),
								   new string[] { string.Format("{0} {1}", items[i].Value.FirstOrDefault(), items[i + 1].Value.FirstOrDefault()), 
												  string.Format("{0} {1}", items[i + 1].Value.FirstOrDefault(), items[i].Value.FirstOrDefault()) });
				}
			}
		}

		#endregion
	}
}
