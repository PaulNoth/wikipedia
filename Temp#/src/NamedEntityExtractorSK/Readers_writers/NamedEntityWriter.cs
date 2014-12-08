using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedEntityExtractorSK.Readers_writers
{
	public static class NamedEntityWriter
	{
		#region Methods

		/// <summary>
		/// Write data into output file
		/// </summary>
		/// <param name="data">Data to write</param>
		/// <param name="fileName">Full file path</param>
		public static void WriteData(List<string> data, string fileName)
		{
			File.WriteAllLines(fileName, data.Where(x => !string.IsNullOrWhiteSpace(x)));
		}

		#endregion
	}

	public static class NamedEntityReader
	{
		#region Methods

		/// <summary>
		/// Read data from input file
		/// </summary>
		/// <param name="fileName">Full file path</param>
		/// <returns></returns>
		public static List<string> ReadData(string fileName)
		{
			var data = File.ReadAllLines(fileName, Encoding.UTF8).ToList();
			return data;
		}

		public static bool FilesExist(params string[] fileNames)
		{
			return !fileNames.Any(x => !File.Exists(x));
		}

		#endregion
	}
}
