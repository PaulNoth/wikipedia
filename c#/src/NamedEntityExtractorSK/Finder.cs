using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using NamedEntityExtractorSK.Data;

namespace NamedEntityExtractorSK
{
	public class Finder
	{
		#region Properties

		private List<string> Persons { get; set; }
		private List<string> Organizations { get; set; }
		private List<string> Locations { get; set; }

		#endregion

		#region Methods

		public Finder(List<string> persons, List<string> organizations, List<string> locations)
		{
			this.Persons = persons;
			this.Organizations = organizations;
			this.Locations = locations;
		}

		public void Find()
		{
			Console.Clear();
			Console.WriteLine("Write words...(for stop write: '#stop')");
			var word = "";

			Console.WriteLine("--------------------------------------------------------------------");
			while ((word = Console.ReadLine()) != "#stop")
			{
				Console.WriteLine(Search(word));
				Console.WriteLine("--------------------------------------------------------------------");
			}
		}

		/// <summary>
		/// Search word by input
		/// </summary>
		/// <param name="word"></param>
		/// <param name="take"></param>
		/// <returns></returns>
		private string Search(string word, int take = 2)
		{
			double minimalSimilarity = 0.7;

			var p = GetMatches(word, take).ToList();
			var mostSimilary = p.Where(x => x.Item2 > 0.99);

			//if some word has similarity > 99%
			if (mostSimilary.Any())
			{
				var winner = mostSimilary.GroupBy(x => x.Item3).ToDictionary(group => group.Key, group => group.ToList())
					.Select(val => new
					{
						Similarity = val.Value.Sum(v => v.Item2),
						Text = string.Format("'{0}' is {1}", word, Enum.GetName(typeof(NamedEntityType), val.Key))
					});

				var max = winner.Max(x => x.Similarity);

				//write output 
				return "Words with 100% similarity:\n" + winner.Where(x => x.Similarity == max).Select(x => x.Text).Aggregate((a, b) => a + "\n" + b);
			}

			//if some word has similarity >= 70%
			if (p.Any(x => x.Item2 >= minimalSimilarity))
			{
				return p.Where(x => x.Item2 >= minimalSimilarity).Select(x => x.Item1).Aggregate((a, b) => a + "\n" + b);
			}
			else
			{
				//No match
				return "Nothing found (with similarity >= 70%)! The most likely word is:\n" + p.FirstOrDefault().Item1;
			}
		}

		/// <summary>
		/// Get matches - result is location/person/organization
		/// </summary>
		/// <param name="word"></param>
		/// <param name="take"></param>
		/// <returns></returns>
		public List<Tuple<string, double, NamedEntityType>> GetMatches(string word, int take)
		{
			var l = Locations.Select(x =>
			{
				var sim = CompareStrings(word, RemoveDiacritics(x));
				return new Tuple<string, double, NamedEntityType>(string.Format("'{0}' is location (sim: {1:0.0%})", x, sim), sim, NamedEntityType.Location);
			})
			.OrderByDescending(t => t.Item2).Take(take).ToList();

			var p = Persons.Select(x =>
			{
				var sim = CompareStrings(word, RemoveDiacritics(x));
				return new Tuple<string, double, NamedEntityType>(string.Format("'{0}' is person (sim: {1:0.0%})", x, sim), sim, NamedEntityType.Person);
			})
			.OrderByDescending(t => t.Item2).Take(take).ToList();

			var o = Organizations.Select(x =>
			{
				var sim = CompareStrings(word, RemoveDiacritics(x));
				return new Tuple<string, double, NamedEntityType>(string.Format("'{0}' is organization (sim: {1:0.0%})", x, sim), sim, NamedEntityType.Organization);
			})
			.OrderByDescending(t => t.Item2).Take(take).ToList();

			l.AddRange(p);
			l.AddRange(o);

			return l.OrderByDescending(x => x.Item2).ToList();
		}

		#region String Comparer

		/// <summary>
		/// Compares the two strings based on letter pair matches
		/// </summary>
		/// <param name="str1"></param>
		/// <param name="str2"></param>
		/// <returns>The percentage match from 0.0 to 1.0 where 1.0 is 100%</returns>
		public double CompareStrings(string str1, string str2)
		{
			List<string> pairs1 = WordLetterPairs(str1.ToUpper());
			List<string> pairs2 = WordLetterPairs(str2.ToUpper());

			int intersection = 0;
			int union = pairs1.Count + pairs2.Count;

			for (int i = 0; i < pairs1.Count; i++)
			{
				for (int j = 0; j < pairs2.Count; j++)
				{
					if (pairs1[i] == pairs2[j])
					{
						intersection++;
						pairs2.RemoveAt(j);//Must remove the match to prevent "GGGG" from appearing to match "GG" with 100% success

						break;
					}
				}
			}

			return (2.0 * intersection) / union;
		}

		/// <summary>
		/// Gets all letter pairs for each
		/// individual word in the string
		/// </summary>
		/// <param name="str"></param>
		/// <returns></returns>
		private List<string> WordLetterPairs(string str)
		{
			List<string> AllPairs = new List<string>();

			// Tokenize the string and put the tokens/words into an array
			string[] Words = Regex.Split(str, @"\s");

			// For each word
			for (int w = 0; w < Words.Length; w++)
			{
				if (!string.IsNullOrEmpty(Words[w]))
				{
					// Find the pairs of characters
					String[] PairsInWord = LetterPairs(Words[w]);

					for (int p = 0; p < PairsInWord.Length; p++)
					{
						AllPairs.Add(PairsInWord[p]);
					}
				}
			}

			return AllPairs;
		}

		/// <summary>
		/// Generates an array containing every
		/// two consecutive letters in the input string
		/// </summary>
		/// <param name="str"></param>
		/// <returns></returns>
		private string[] LetterPairs(string str)
		{
			int numPairs = str.Length - 1;

			string[] pairs = new string[numPairs];

			for (int i = 0; i < numPairs; i++)
			{
				pairs[i] = str.Substring(i, 2);
			}

			return pairs;
		}

		#endregion

		private string RemoveDiacritics(string text)
		{
			return string.Concat(
				text.Normalize(NormalizationForm.FormD)
				.Where(ch => CharUnicodeInfo.GetUnicodeCategory(ch) !=
											  UnicodeCategory.NonSpacingMark)
			  ).Normalize(NormalizationForm.FormC);
		}

		#endregion
	}
}
