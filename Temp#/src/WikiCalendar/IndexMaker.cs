using Lucene.Net.Analysis;
using Lucene.Net.Analysis.Standard;
using Lucene.Net.Documents;
using Lucene.Net.Index;
using Lucene.Net.QueryParsers;
using Lucene.Net.Search;
using Lucene.Net.Store;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WikiCalendar
{
	/// <summary>
	/// Class is used for index making and index reading. Once index is made its not neccesarry to make it again with new run of application. It will read previously made index.
	/// </summary>
	public class IndexMaker
	{
	private const string DOC_ID_FIELD_NAME = "ID_FIELD";
 
    private string _fieldName;
    private string _indexDir;

	public IndexMaker(string indexDir, string fieldName)
	{

        _indexDir = indexDir;
        _fieldName = fieldName;
		using (Directory directory = FSDirectory.Open(_indexDir))
		using (Analyzer analyzer = new StandardAnalyzer(Lucene.Net.Util.Version.LUCENE_30))
		using (IndexWriter writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.UNLIMITED))
		{
			writer.DeleteAll();
		}
    }
 
    /// <summary>
    /// This method indexes the content that is sent across to it. Each piece of content (or "document")
    /// that is indexed has to have a unique identifier (so that the caller can take action based on the
    /// document id). Therefore, this method accepts key-value pairs in the form of a dictionary. The key
    /// is a ulong which uniquely identifies the string to be indexed. The string itself is the value
    /// within the dictionary for that key. Be aware that stop words (like the, this, at, etc.) are _not_
    /// indexed.
    /// </summary>
    /// <param name="txtIdPairToBeIndexed">A dictionary of key-value pairs that are sent by the caller
    /// to uniquely identify each string that is to be indexed.</param>
    /// <returns>The number of documents indexed.</returns>
    public int Index (Dictionary<long, string> txtIdPairToBeIndexed) {

		using (Directory directory = FSDirectory.Open(_indexDir))
		using (Analyzer analyzer = new StandardAnalyzer(Lucene.Net.Util.Version.LUCENE_30))
		using (IndexWriter writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.UNLIMITED))
		using (IndexReader reader = writer.GetReader())
		{

			//writer.DeleteAll();


			Dictionary<long, string>.KeyCollection keys = txtIdPairToBeIndexed.Keys;

			foreach (long id in keys)
			{
				char[] delimiter = { ';' };
				string[] text = txtIdPairToBeIndexed[id].Split(delimiter);
				Document document = new Document();

				Field title = new	Field("title", text[0], Field.Store.YES, Field.Index.NO);
				Field type = new Field("type", text[1], Field.Store.YES, Field.Index.NO);
				Field idField = new Field("date", (id).ToString(), Field.Store.YES, Field.Index.ANALYZED);

				document.Add(title);
				document.Add(type);
				document.Add(idField);

				writer.AddDocument(document);
			}

			int numIndexed = writer.GetDocCount(0);//TODO check number
			writer.Optimize();
			writer.Flush(true,true,true);

			return numIndexed;
		}

		}
   
 
    /// <summary>
    /// This method searches for the search term passed by the caller.
    /// </summary>
    /// <param name="searchTerm">The search term as a string that the caller wants to search for within the
    /// index as referenced by this object.</param>
    /// <param name="ids">An out parameter that is populated by this method for the caller with docments ids.</param>
    /// <param name="results">An out parameter that is populated by this method for the caller with docments text.</param>
    /// <param name="scores">An out parameter that is populated by this method for the caller with docments scores.</param>
    public void Search (string searchTerm, out long[] ids, out string[] results) {
		using (Directory directory = FSDirectory.Open(_indexDir))
		{
			//QueryParser queryParser = new QueryParser(_fieldName, new StandardAnalyzer());
			Query query = new TermQuery(new Term("date", searchTerm));

			IndexSearcher indexSearcher = new IndexSearcher(directory);

			var hits = indexSearcher.Search(query,20);

			
			int numHits = hits.TotalHits;

			ids = new long[numHits];
			results = new string[numHits];

			for (int i = 0; i < numHits; ++i)
			{
				var actualDoc = indexSearcher.Doc(hits.ScoreDocs[i].Doc);
				var date = actualDoc.GetField("date").StringValue;
				var title = actualDoc.GetField("title").StringValue;

				ids[i] = Int64.Parse(date);
				results[i] = title ;
			}
		}
    }
	}
}
