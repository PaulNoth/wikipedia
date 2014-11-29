using System.Diagnostics;

namespace NamedEntityExtractorSK.Data
{
	/// <summary>
	/// Data class for geobox
	/// </summary>
	[DebuggerDisplay("Content = {Content}")]
	public class Geobox : KnowlegeData
	{
		#region Properties
		#endregion

		#region Methods

		public override void SetRegexAttributes()
		{
			//locations
			//mayor → person
			this.Attributes = new RegexKeyType[] {	new RegexKeyType("[Mm]eno[0-9]*", NamedEntityType.Location),
													new RegexKeyType("[nN]ame", NamedEntityType.Location),
													new RegexKeyType("[oO]ther_name", NamedEntityType.Location),
													new RegexKeyType("[Cc]ountry", NamedEntityType.Location),
													new RegexKeyType("[rR]egi[oó]n", NamedEntityType.Location),
													new RegexKeyType("[mM]ayor", NamedEntityType.Person)};
		}

		#endregion
	}
}
