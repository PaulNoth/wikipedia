using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using NamedEntityExtractorSK.Data;

namespace NamedEntityExtractorSK.Utilities
{
	public static class WordUtils
	{
		public static string[] Citacia = { "{{Citácia" };
		public static string[] Infobox = { "{{Infobox" };
		public static string[] Geobox = { "{{Geobox" };

		public static string[] SearchBoxes = { "{{Citácia", "{{Infobox", "{{Geobox" };

		private static char[] WhiteSpaces = new char[] { ' ', '\n', '\t' };

		private static char[] NonLetterCharacters = new char[] { ' ', '|', '{', '}', '[', ']', '"', ',', '+', '-', '–', '*' };
		private static string[] HtmlCharacters = new string[] { "&lt;", "&gt;", "&amp;", "/ref", "&quot;", "/sub", "***", "!--" };

		public static string TrimWhiteSpaces(this string text)
		{
			text = text.TrimStart(WhiteSpaces);
			return text.TrimEnd(WhiteSpaces);
		}

		/// <summary>
		/// Trim end chars from infobox/geobox/citation
		/// </summary>
		/// <typeparam name="T"></typeparam>
		/// <param name="splited"></param>
		/// <param name="data"></param>
		public static void TrimBoxes<T>(IEnumerable<string> splited, ref List<KnowlegeData> data)
			where T : KnowlegeData
		{
			foreach (var item in splited)
			{
				var index = item.IndexOf("}}");

				if (index != -1)
				{
					T instance = (T) Activator.CreateInstance(typeof(T));
					instance.Content = item.Remove(index);
					
					data.Add(instance);
				}
			}
		}

		/// <summary>
		/// Trim defined characters from input word
		/// </summary>
		/// <param name="word">Input word</param>
		/// <param name="deleteNumbers">Remove numbers from the word</param>
		/// <returns>Trimmed word</returns>
		public static string TrimNonLetterCharacters(string word, bool deleteNumbers = false)
		{
			word = Regex.Replace(word, @"&[a-z]*;", "");
			word = Regex.Replace(word, @"/ref", "");
			word = Regex.Replace(word, @"/sub", "");
			word = Regex.Replace(word, @"\*\*\*", "");
			word = Regex.Replace(word, @"!--", "");
			word = Regex.Replace(word, @"[()]", "");
			word = Regex.Replace(word, @"['\\,\?]", "");
			word = Regex.Replace(word, @"\n", "");
			word = deleteNumbers ? Regex.Replace(word, @"[\d.,]", "") : word.TrimStart(' ').TrimEnd(' ');
			return word.TrimStart(NonLetterCharacters).TrimEnd(NonLetterCharacters);
		}
	}
}
