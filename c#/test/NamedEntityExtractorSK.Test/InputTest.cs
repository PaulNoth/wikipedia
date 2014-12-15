using System;
using System.Linq;
using System.Xml;
using NamedEntityExtractorSK;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using NamedEntityExtractorSK.Readers_writers;
using NamedEntityExtractorSK.Utilities;
using NamedEntityExtractorSK.Data;
using System.IO;

namespace NamedEntityExtractorSK.Test
{
	[TestClass]
	public class InputTest
	{
		/// <summary>
		/// Check if Input file has some infobox
		/// </summary>
		[TestMethod]
		public void HasAnyInfoboxes()
		{
			var page = GetPage();

			Assert.AreEqual(page.Infoboxes.Any(), true);
		}

		/// <summary>
		/// Check if first infobox has 2 items
		/// </summary>
		[TestMethod]
		public void HasTwoItems()
		{
			var page = GetPage();

			var item = page.Infoboxes[0];

			Assert.AreEqual(item.Items.Values.Count(), 2);
		}

		/// <summary>
		/// Check if input file contains infobox 'Isaac Newton' and it is person
		/// </summary>
		[TestMethod]
		public void ContainsPersonIsaacNewton()
		{
			var page = GetPage();

			var item = page.Infoboxes[0];

			var key = new RegexKeyType("Meno", NamedEntityType.Person);
			var value = "Isaac Newton";
			
			Assert.AreEqual(item.Items[key][0], value);
		}

		/// <summary>
		/// Check if input file contains location 'Woolsthorpe-by-Colsterworth'
		/// </summary>
		[TestMethod]
		public void ContainsLocationWoolsthorpeByColsterworth()
		{
			var page = GetPage();

			var item = page.Infoboxes[0];

			var key = new RegexKeyType("Miesto narodenia", NamedEntityType.Location);
			var value = "Woolsthorpe-by-Colsterworth";

			Assert.AreEqual(item.Items[key][0], value);
		}

		/// <summary>
		/// Get issac newton page
		/// </summary>
		/// <returns></returns>
		private Page GetPage()
		{
			var inputFilePath = GetDataPath();

			Page page = null;

			using (var reader = XmlReader.Create(inputFilePath))
			{
				while (reader.Read())
				{
					if (reader.NodeType == XmlNodeType.Element && reader.Name.Equals("page"))
					{
						var outerXml = reader.ReadOuterXml();

						if (outerXml.Contains("<title>Isaac Newton</title>"))
						{
							var infobox = outerXml.Split(WordUtils.Infobox, System.StringSplitOptions.RemoveEmptyEntries).Skip(1);
							var data = new List<KnowlegeData>();

							WordUtils.TrimBoxes<Infobox>(infobox, ref data);

							page = new Page(data);
							page.Infoboxes.ForEach(x => x.SetRegexAttributes());
							break;
						}
					}
				}
			}

			if (page == null)
				new AssertFailedException("Page not found");

			return page;
		}

		/// <summary>
		/// Check if input file exist
		/// </summary>
		[TestMethod]
		public void InputFileExists()
		{
			var inputFilePath = GetDataPath();

			var result = File.Exists(inputFilePath);

			Assert.IsTrue(result);
		}

		/// <summary>
		/// Check if input file contains sections (infobox/geobox/citation)
		/// </summary>
		[TestMethod]
		public void ContainsSections()
		{
			var data = new List<KnowlegeData>();
			var inputFilePath = GetDataPath();

			using (var reader = XmlReader.Create(inputFilePath))
			{
				while (reader.Read())
				{
					if (reader.NodeType == XmlNodeType.Element && reader.Name.Equals("page"))
					{
						var outerXml = reader.ReadOuterXml();

						var infoboxes = outerXml.Split(WordUtils.Infobox, System.StringSplitOptions.RemoveEmptyEntries).Skip(1);
						var geoboxes = outerXml.Split(WordUtils.Geobox, System.StringSplitOptions.RemoveEmptyEntries).Skip(1);
						var citations = outerXml.Split(WordUtils.Citacia, System.StringSplitOptions.RemoveEmptyEntries).Skip(1);

						if (infoboxes.Any() || geoboxes.Any() || citations.Any())
						{
							WordUtils.TrimBoxes<Infobox>(infoboxes, ref data);
							WordUtils.TrimBoxes<Geobox>(geoboxes, ref data);
							WordUtils.TrimBoxes<Citation>(citations, ref data);

							break;
						}
					}
				}
			}

			Assert.IsTrue(data.Any());
		}

		/// <summary>
		/// Check if input file contains some page
		/// </summary>
		[TestMethod]
		public void ContainsPages()
		{
			var somePages = false;
			var inputFilePath = GetDataPath();

			using (var reader = XmlReader.Create(inputFilePath))
			{
				while (reader.Read())
				{
					if (reader.NodeType == XmlNodeType.Element && reader.Name.Equals("page"))
					{
						somePages = true;
						break;
					}
				}
			}

			Assert.IsTrue(somePages);
		}

		public string GetDataPath()
		{
			var baseDirectory = AppDomain.CurrentDomain.BaseDirectory.Split('\\');
			var directory = baseDirectory.Take(baseDirectory.Length - 5).Aggregate((a, b) => a + '\\' + b);
			return directory + @"\data\sample_input_skwiki-latest-pages-articles.xml";
		}
	}
}
