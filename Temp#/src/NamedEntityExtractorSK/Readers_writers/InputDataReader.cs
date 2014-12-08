using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Data.Linq;
using System.IO;
using System.Xml;
using System.Xml.Linq;
using NamedEntityExtractorSK.Data;
using NamedEntityExtractorSK.Utilities;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace NamedEntityExtractorSK.Readers_writers
{
	public class InputDataReader
	{
		#region Properties

		public List<Page> Pages { get; private set; }

		#endregion

		#region Methods

		public InputDataReader()
		{
			this.Pages = new List<Page>();
		}

		/// <summary>
		/// Read XML - get pages structure
		/// </summary>
		/// <param name="inputFilePath"></param>
		public void SetPagesFromInputFile(string inputFilePath)
		{
			using (var reader = XmlReader.Create(inputFilePath))
			{
				while (reader.Read())
				{
					if (reader.NodeType == XmlNodeType.Element && reader.Name.Equals("page"))
					{
						var outerXml = reader.ReadOuterXml();

						//paralel work
						Parallel.Invoke(() =>
							{
								//get all infoboxes from page
								var infoboxes = outerXml.Split(WordUtils.Infobox, System.StringSplitOptions.RemoveEmptyEntries).Skip(1);
								//get all geoboxes from page
								var geoboxes = outerXml.Split(WordUtils.Geobox, System.StringSplitOptions.RemoveEmptyEntries).Skip(1);
								//get all citations from page
								var citations = outerXml.Split(WordUtils.Citacia, System.StringSplitOptions.RemoveEmptyEntries).Skip(1);

								if (infoboxes.Any() || geoboxes.Any() || citations.Any())
								{
									var data = new List<KnowlegeData>();

									WordUtils.TrimBoxes<Infobox>(infoboxes, ref data);
									WordUtils.TrimBoxes<Geobox>(geoboxes, ref data);
									WordUtils.TrimBoxes<Citation>(citations, ref data);

									if (data.Any()) Pages.Add(new Page(data));
								}
							});
					}
				}
			}
		}

		#endregion
	}
}
