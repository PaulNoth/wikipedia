using System.Collections.Generic;
using System.Threading.Tasks;

namespace NamedEntityExtractorSK.Data
{
	/// <summary>
	/// Data class for Page
	/// </summary>
	public class Page
	{
		#region Properties

		public List<KnowlegeData> Infoboxes { get; set; }

		#endregion

		#region Constructors

		public Page(List<KnowlegeData> data)
		{
			Infoboxes = data;
		}

		#endregion
	}
}
