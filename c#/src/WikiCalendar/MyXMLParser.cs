using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Xml.Linq;
using System.Collections;

namespace WikiCalendar
{
	/// <summary>
	/// Xml parser with storage for all events. It parses wiki xml dump sent as parameter to constructor. It has functionality like Controller. 
	/// It uses Indexes to search for events or return all events information
	/// </summary>
	public class MyXMLParser
	{
		public MyXMLParser()
		{
			xml = new XElement("days");
			allEvents = new LinkedList<CalendarEvent>();
			allDays = new SortedList<long, DayEventCollection>();
			index = new IndexMaker("LuceneIndex","date");
			pageCounter = 0;
		}
		public long pagesCount { get; set; }
		public long eventsCount { get; set; }
		public int pageCounter{get;set;}
		XElement xml { get; set; }
		LinkedList<CalendarEvent> allEvents;
		SortedList<long, DayEventCollection> allDays;
		IndexMaker index;

		public void initParsing(String path)
		{
			
			//XDocument input = XDocument.Load(@"..\..\..\..\Data\input.xml");
			//XDocument input = XDocument.Load(@"D:\downNew\wiki\enwiki-latest-pages-articles1.xml-p000000010p000010000");
			XDocument input = XDocument.Load(path);
			// Console.WriteLine(booksFromFile);


			//CalendarEvent ce = new CalendarEvent(input);
			//var pages = input.Root.Descendants("page");
			XElement eee = input.Element("siteinfo");
			IEnumerable<XElement> pages = input.Element("mediawiki").Elements("page");

			Console.WriteLine(pages.Count().ToString());
			pagesCount = pages.Count();
			
			//pagesCountTextBox.Text = pages.Count().ToString();		


			// matching infobox from page text
			
			
			pageCounter = 0;
			foreach(XElement page in pages)
			{
				String infoboxPattern = "{{Infobox (.+\n)+}}";
				MatchCollection infoboxes = System.Text.RegularExpressions.Regex.Matches(page.Element("revision").Element("text").Value, infoboxPattern, System.Text.RegularExpressions.RegexOptions.Multiline);

				//99% just one iteration
				foreach (Match infobox in infoboxes)
				{
					String datePattern = "[A-Za-z_]+date\\s+=.+";//TODO optimize!
					MatchCollection dateLine = Regex.Matches(infobox.Value,datePattern);
					
					//statisticaly 2 iterations
					foreach( Match n in dateLine)
					{
						//TODO check release_date - just year 
						//pub_date


					   // Console.WriteLine(n.Value);
						//dateFormat is Y|MM|DD or 'BCE'|Y 
						String dateExtractPattern = ""
							//"([0-9]{0,}\\|[0-1]{0,1}[0-9]{1}\\|[0-3]{0,1}[0-9]{1})|(BCE\\|[0-9]+)"
							+ @"([0-9]{1,4}\|[0-1]{0,1}[0-9]{1}\|[0-3]{0,1}[0-9]{1})" //Y|MM|DD
							+ @"|((?:(BC)?|(BCE)?)\|[0-9]+(\|\d\|\d){0})" //'BCE'|Y 
							+ @"|((?:([0-3]?[0-9]) ((?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) )((\d+(\sBCE)?|(\sBC)?)|((AD\s)?\d{1,})))" //3 July 2001
							+ @"|((?:(?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) (?:([0-3]?[0-9])), [0-9]{0,}\s?(?:(BC)?|(BCE)?))"// July 3, 2001
							+ @"|((?:([0-3]?[0-9]) ((?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) )(?:(AD)?)\s[0-9]{1,})"; //3 July AD 2001
						MatchCollection dateExtract = Regex.Matches(n.Value, dateExtractPattern);
						//foreach (Match o in dateExtract)
						//{
						//	allEvents.AddLast(new CalendarEvent(page));
						//	allEvents.Last.Value.setDates(o.Value, dateExtractPattern);
						//	//Console.WriteLine(o.Value);
						//}

						//dateExtractPattern = @"|((?:(BC)?|(BCE)?)\|[0-9]+)"; //'BCE'|Y 
						//dateExtract = Regex.Matches(n.Value, dateExtractPattern);
						//foreach (Match o in dateExtract)
						//{
						//	allEvents.AddLast(new CalendarEvent(page));
						//	allEvents.Last.Value.setDates(o.Value, dateExtractPattern);
						//	//Console.WriteLine(o.Value);
						//}
						////+ "|([0-3]{0,1}[0-9]{1} \b(?:(January)?|(February)?|(March)?|(April)?|(May)?|(June)?|(July)?|(August)?|(September)?|(October)?|(November)?|(December)?) [0-9]{0,}\\s?(?:(BC)?|(BCE)?))" 
						////+ "(?:([0-3]{0,1}[0-9]{1}) (\b(?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) ([0-9]{0,})(\s?(?:BC)?|(?:BCE)?))";
						//dateExtractPattern = @"|((?:([0-3]?[0-9]) ((?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) )[0-9]{0,}\s?(?:(BC)?|(BCE)?))"; //3 July 2001
						//dateExtract = Regex.Matches(n.Value, dateExtractPattern);
						//foreach (Match o in dateExtract)
						//{
						//	allEvents.AddLast(new CalendarEvent(page));
						//	allEvents.Last.Value.setDates(o.Value, dateExtractPattern);
						//	//Console.WriteLine(o.Value);
						//}

						//dateExtractPattern = @"|(?:(?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) (?:([0-3]?[0-9])), [0-9]{0,}\s?(?:(BC)?|(BCE)?)"; // July 3, 2001
						//dateExtract = Regex.Matches(n.Value, dateExtractPattern);
						// Console.WriteLine("dates formated Y|MM|DD or 'BCE'|Y ");
						//1 iteration
						foreach (Match o in dateExtract)
						{
							CalendarEvent extractedEvent = new CalendarEvent(page);
							try
							{
								extractedEvent.setDates(o.Value, dateExtractPattern,n.Value);											
							}
							catch (DataMisalignedException exc) 
							{
								Console.WriteLine(infobox);
								Console.WriteLine( exc.Message);
								continue;
							}

							allEvents.AddLast(extractedEvent);

							Dictionary<long, string> indexing = new Dictionary<long, string>();
							indexing.Add(extractedEvent.dateId, extractedEvent.title + ";" + extractedEvent.eventType );
							index.Index(indexing);

							if(allDays.Keys.Contains(extractedEvent.dateId))
							{
								allDays[extractedEvent.dateId].Add(extractedEvent);


							}
							else
							{

								allDays.Add(extractedEvent.dateId, new DayEventCollection(extractedEvent.date.Day, extractedEvent.date.Month, extractedEvent.date.Year, extractedEvent));
							}


							//Console.WriteLine(o.Value);
							eventsCount = allEvents.Count;
						}
						/*
						 * |birth_date={{birth date|1809|2|12}} 
						 * | birth_date = {{birth date|mf=yes|1905|2|2}}
						 * birth_date  = {{birth date and age|1947|04|01|df=y}} 
						 * | birth_date = {{Birth date|df=yes|1885|4|3}}
						 * | birth_date = {{birth date and age|1970|04|29}}
						 * | birth_date = {{birth date|df=yes|1894|7|26}}
						 * |birth_date= {{Birth date|1803|2|2}}
						 */
					}
				
				}
				pageCounter++;
				//this.pagesProgresBar.Value = (int)((100 * pageCounter) / pages.Count());
				
				
			}
			Console.WriteLine(allEvents.Count);

		}
		public void exportEventsXML(String path) 
		{
			foreach (CalendarEvent ce in allEvents)
			{
				xml.Add(ce.exportXML());
			}
			//Console.WriteLine(xml.ToString());
			saveXml(path);
		}

		private void saveXml(String path)
		{
			xml.Save(path);
		}

		public string getXmlString()
		{
			return xml.ToString();
		}

		internal HashSet<CalendarEvent> searchDayEvents(String dateKey)
		{
			long[] ids;
			string[] results;

			index.Search(dateKey, out ids, out results);

			HashSet<CalendarEvent> returnSet = new HashSet<CalendarEvent>();
			
			for (int k = 0;k<ids.Length;k++)
			{
				returnSet.Add(new CalendarEvent() { dateId = ids[k], title = results[k] });

			}
			return returnSet;
			//long parsedKey;
			//if(long.TryParse(dateKey,out parsedKey) && allDays.Keys.Contains(parsedKey)){

			//	DayEvents d = allDays[parsedKey];
			//	return d.getEventsArray();
			//}
			//else
			//{
			//	var x = from pKey in allDays.Keys
			//			where pKey.ToString().Contains(dateKey)
			//			select pKey.ToString();

			//	if (x.Any())
			//	{
			//		HashSet<CalendarEvent> returnSet = new HashSet<CalendarEvent>();
			//		foreach( var i in x)
			//		{
			//			parsedKey = long.Parse(i);
			//			DayEvents d = allDays[parsedKey];
			//			var gettedEvents = d.getEventsArray();
			//			foreach (var eve in gettedEvents)
			//			{
			//				returnSet.Add(eve);
			//			}
						

			//		}

			//		return returnSet;
			//	}
			//	else return null;
				
			//}

			
		}

		public List<long> getAllDays()
		{
			List<long> keys = allDays.Keys.ToList<long>();
			return keys;
		}
		public List<String> getDistinctEventTypes()
		{
			var types = from e in allEvents
						select e.eventType;

			return types.Distinct().ToList<String>();
		}

		public string getEventStatistics()
		{
			var statisticLines = allEvents
					.GroupBy(ce => ce.eventType)
					.Select(group => new {eventType = group.Key, typeCount = group.Count()})
					.OrderBy(x => x.typeCount);

			string returnString = "";
			foreach(var statisticLine in statisticLines)
			{
				returnString += statisticLine.eventType +":"+ statisticLine.typeCount.ToString() + "\n";
			}
			return returnString;
		}
	}
}
