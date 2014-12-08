using System;
using System.Linq;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using NamedEntityExtractorSK.Utilities;

namespace NamedEntityExtractorSK.Data
{
	public class KnowlegeData
	{
		#region Fields

		private Lazy<Dictionary<RegexKeyType, string[]>> _Items;

		#endregion

		#region Properties

		public string Content { get; set; }

		public Dictionary<RegexKeyType, string[]> Items
		{
			get
			{
				return this._Items.Value;
			}
		}

		/// <summary>
		/// Regex attributes names
		/// </summary>
		public RegexKeyType[] Attributes { get; set; }

		#endregion

		#region Methods

		public KnowlegeData()
		{
			this._Items = new Lazy<Dictionary<RegexKeyType, string[]>>(LoadItems);
		}

		/// <summary>
		/// Load items from KnowledgeData part - split by '|' and decoding type of item
		/// </summary>
		/// <returns></returns>
		private Dictionary<RegexKeyType, string[]> LoadItems()
		{
			var items = new Dictionary<RegexKeyType, string[]>();
			this.Content = this.Content.Replace("|", " | ");

			Regex.Split(this.Content, @"\s\| ")
						.Where(word => word.Contains("=")).ToList()
						.ForEach(item =>
						{
							var word = Regex.Split(item, "=");
							if (word.Count() == 2)
							{
								var key = WordUtils.TrimWhiteSpaces(word[0]);
								var value = WordUtils.TrimWhiteSpaces(word[1]);

								if (!string.IsNullOrWhiteSpace(key) && !string.IsNullOrWhiteSpace(value))
								{
									var values = SplitMultipleValues(value);
									var type = new RegexKeyType();
									type.Value = key;

									try
									{
										items.Add(type, values);
									}
									catch { }
								}
							}
						});

			return items.Where(i => Attributes.Any(a => Regex.IsMatch(i.Key.Value, a.Value)))
						.ToDictionary(i => {
												var key = i.Key;
												key.Type = Attributes.FirstOrDefault(a => Regex.Match(key.Value, a.Value).Success).Type;
												return key;
											},
									  i => i.Value);
			//Attributes.FirstOrDefault(a => Regex.Match(i.Key, a).Success)
		}

		// add items between [[ ]] - split and AddRange()
		// between [[ ]] may be contains '|' → split expressions
		public string[] SplitMultipleValues(string value)
		{
			if (value.Contains("[["))
			{
				string pattern = @"\[\[(.*?)\]\]";
				var values = new List<string>();

				Regex.Matches(value, pattern).Cast<Match>()
					 .Select(m => m.Value.TrimStart('[').TrimEnd(']').Split('|')).ToList().ForEach(x => values.AddRange(x.Select(v => v)));

				return values.ToArray();
			}
			else
				return new string[] { value };			
		}

		public virtual void SetRegexAttributes() { }

		#endregion
	}
}
