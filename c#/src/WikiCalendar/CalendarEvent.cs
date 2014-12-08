using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace WikiCalendar
{
	/// <summary>
	/// Structure for storing informtaion about event.
	/// </summary>
	public class CalendarEvent
	{
		public DateTime date { get; set; } //TODO parsing dates of wiki
		/// <summary>
		/// BC can reach -1 - date is BC 
		/// or +1 date is AD
		/// </summary>
		public int BC { get; set; }
		public long dateOffset { get; set; }
		public String description {get; set;}
		public String title {get;set;}
		public long id { get; set; }
		public String text { get; set; }
		public String eventType { get; set; }
		public long dateId { get; set; }

		public CalendarEvent(){}

		private String extractEventType(String _dateLine)
		{
			String _eventType = "";
			String[] wordsLine = _dateLine.Split(new char[] { ' ' ,'='});
			_eventType = wordsLine[0].Replace("_"," ").Trim();
						
			return _eventType;
		}
		public void setDates(String _input, String dateExtractPattern,String eventDateLine)
		{
			BC = 1;
			eventType = extractEventType(eventDateLine);

			String[] delimiter = { ")|(" };
			String[] datePaterns = dateExtractPattern.Split(delimiter,StringSplitOptions.RemoveEmptyEntries);
			CultureInfo provider = CultureInfo.InvariantCulture;
			//bracets after sprlit removal
			
			StringBuilder sb = new StringBuilder(datePaterns[0]);
			int i = 0;
			datePaterns[i] = sb.Insert(sb.Length,")",1).ToString();

			for (i = 1; i < 4; i++)
			{
				sb = new StringBuilder(datePaterns[i]);
				datePaterns[i] = sb.Insert(0, "(", 1).ToString();
				datePaterns[i] = sb.Insert(sb.Length,")",  1).ToString();
			}
			sb = new StringBuilder(datePaterns[i]);
			datePaterns[i] = sb.Insert(0, "(", 1).ToString();

			String[] delimiter2 = { " " };
			if (_input.Contains(" AD"))
			{
				_input=_input.Replace(" AD", " ");
			}
			_input=_input.Trim().Trim('}');

			if (_input.Contains("BC") || _input.Contains("BCE"))
			{
				BC = -1;
				throw new DataMisalignedException();
			}

			if (_input.Contains("|"))
			{
				String[]delimiterPipe = {"|"};
				String[] separatedDate = _input.Split(delimiterPipe, StringSplitOptions.RemoveEmptyEntries);
				int offset = 0;
				if (BC == -1/* && (separatedDate[offset].Contains("BC") || separatedDate[offset].Contains("BCE"))*/)
				{
					offset++;
				}
				int year = int.Parse(separatedDate[offset++]);
				int month = 1, day = 1;
				if (/*!(separatedDate[0].Contains("BC") || separatedDate[0].Contains("BCE")) && */separatedDate.Length >= 2 && int.Parse(separatedDate[offset]) != 0 )
				{
					month = int.Parse(separatedDate[offset++]);
				}
				if (/*!(separatedDate[0].Contains("BC") || separatedDate[0].Contains("BCE")) &&*/ separatedDate.Length >= 3 && int.Parse(separatedDate[offset]) != 0 )
				{
					day = int.Parse(separatedDate[offset++]);
				}

				date = new DateTime(year, month, day);
				setDateId(day, month, year);
			}
			else if (_input.Contains(","))
			{
				_input = _input.Replace(", ", " ");
				String[] separatedDate = _input.Split(delimiter2, StringSplitOptions.RemoveEmptyEntries);
				
				int month = 1, day = 1,year = 1;
				if (separatedDate.Length < 3)
				{
					throw new DataMisalignedException("not date format");
				}

				month = getMonthFromText(separatedDate[0]);
				day = int.Parse(separatedDate[1]);
				year = int.Parse(separatedDate[2]);

				date = new DateTime(year, month, day);
				setDateId(day, month, year);
			}
			else 
			{
				String[] separatedDate = _input.Split(delimiter2, StringSplitOptions.RemoveEmptyEntries);
				
				int month = 1, day = 1, year = 1;
				if (separatedDate.Length >= 1)
				{
					day = int.Parse(separatedDate[0]);
				}
				if (separatedDate.Length >= 2)
				{
					month = getMonthFromText(separatedDate[1]);
				}
				if (separatedDate.Length >= 3)
				{
					year = int.Parse(separatedDate[2]);
				}

				date = new DateTime(year, month, day);
				setDateId(day, month, year);

			}
							




			//if (System.Text.RegularExpressions.Regex.IsMatch(_input, datePaterns[0]))//Y|MM|DD
			//{

			//	//date = DateTime.ParseExact(_input, "yyyy|M|d", provider);
			//	String[] separatedDate = _input.Split('|');
			//	int day = int.Parse(separatedDate[2]);
			//	int month = int.Parse(separatedDate[1]);
			//	int year = int.Parse(separatedDate[0]);

			//	date = new DateTime(year, month, day);
			//}
			//else if (System.Text.RegularExpressions.Regex.IsMatch(_input, datePaterns[1])) //'BCE'|Y 
			//{
			//	if (_input.Contains("BC"))
			//	{
			//		//_input = _input.Split('|')[1];
			//	}
			//	//workaround
			//	_input = _input.Split('|')[1];
			//	_input = _input.Split('}')[0];
			//	date = new DateTime(int.Parse(_input),1,1);
			//	//date = DateTime.ParseExact(_input, "y", provider);
			//}
			//else if (System.Text.RegularExpressions.Regex.IsMatch(_input, datePaterns[2])) //3 July 2001
			//{
				
			//	String[] separatedDate = _input.Split(delimiter2, StringSplitOptions.RemoveEmptyEntries);
			//	int day = int.Parse(separatedDate[0]);
			//	int month = getMonthFromText(separatedDate[1]);
			//	int year = int.Parse(separatedDate[2]);

			//	date = new DateTime(year, month, day);

			//	//date = DateTime.ParseExact(_input, "d MMMM yyyy", provider);
			//}
			//else if (System.Text.RegularExpressions.Regex.IsMatch(_input, datePaterns[3]))// July 3, 2001
			//{
			//	//parsing comma 
			//	_input = _input.Replace(", "," ");
			//	String[] separatedDate = _input.Split(delimiter2, StringSplitOptions.RemoveEmptyEntries);
			//	int month = getMonthFromText(separatedDate[0]);
			//	int day = int.Parse(separatedDate[1]);
			//	int year = int.Parse(separatedDate[2]);

			//	date = new DateTime(year, month, day);
			//		//DateTime.ParseExact(_input, "MMMM d yyyy", provider);
			//}
			//else if (System.Text.RegularExpressions.Regex.IsMatch(_input, datePaterns[4]))//3 July AD 2001
			//{
			//	if (_input.Contains(" AD"))
			//	{
			//		_input.Replace(" AD", " ");
			//	}
			//	_input = _input.Replace(", ", " ");
				
			//	String[] separatedDate = _input.Split(delimiter2, StringSplitOptions.RemoveEmptyEntries);
			//	int day = int.Parse(separatedDate[0]);
			//	int month = getMonthFromText(separatedDate[1]);
			//	int year = int.Parse(separatedDate[2]);

			//	date = new DateTime(year, month, day);

			//	//DateTime.ParseExact(_input, "MMMM d yyyy", provider);
			//}
			//else throw new Exception();//we are doomed

			//Console.WriteLine(date.ToString());
			/*
					   "([0-9]{0,4}\\|[0-1]{0,1}[0-9]{1}\\|[0-3]{0,1}[0-9]{1})" //Y|MM|DD
						+ "|((?:(BC)?|(BCE)?)\\|[0-9]+)" //'BCE'|Y 
						//+ "|([0-3]{0,1}[0-9]{1} \b(?:(January)?|(February)?|(March)?|(April)?|(May)?|(June)?|(July)?|(August)?|(September)?|(October)?|(November)?|(December)?) [0-9]{0,}\\s?(?:(BC)?|(BCE)?))" 
						//+ "(?:([0-3]{0,1}[0-9]{1}) (\b(?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) ([0-9]{0,})(\s?(?:BC)?|(?:BCE)?))";
						+ "|((?:([0-3]?[0-9]) ((?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) )[0-9]{0,}\\s?(?:(BC)?|(BCE)?))" //3 July 2001
						+ "|(?:(?:January)?|(?:February)?|(?:March)?|(?:April)?|(?:May)?|(?:June)?|(?:July)?|(?:August)?|(?:September)?|(?:October)?|(?:November)?|(?:December)?) (?:([0-3]?[0-9])), [0-9]{0,}\\s?(?:(BC)?|(BCE)?)"; // July 3, 2001
 */
			//if(_input==)
		}
		public CalendarEvent(XElement page)
		{
			//XElement page = input.Element("page");
			title = page.Element("title").Value;
			id = long.Parse(page.Element("id").Value);
			text = page.Element("revision").Element("text").Value;

			//describeEvent();

		}
		private short getMonthFromText(String text)
		{
			switch (text){
				case "January": return 1;
				case "February": return 2;
				case "March": return 3;
				case "April": return 4;
				case "May": return 5;
				case "June": return 6;
				case "July": return 7;
				case "August": return 8;
				case "September": return 9;
				case "October": return 10;
				case "November": return 11;
				case "December": return 12;
				default: throw new DataMisalignedException("not date format");

			}
				

			 
		}
	
		void describeEvent()
		{
			StringBuilder sb = new StringBuilder();

			sb.AppendLine("Title:" + title);
			sb.AppendLine("ID: " + id);
			sb.AppendLine("Text:" + text);

			Console.WriteLine(sb.ToString());
		}

		public XElement exportXML()
		{
			XElement eventXml = new XElement("day",
				new XElement("date",date.ToString("yyyy|MM|dd")),
				new XElement("events",
					new XElement("event",
						new XElement("event_title",title),
						new XElement("event_type",eventType),
						new XElement("page_id",id.ToString())
					)
				)
			);
			return eventXml;

		}
		public void setDateId(int _day, int _month, int _year)
		{
			dateId = BC *_year * 10000 + _month * 100 + _day;
		}
	}
}
