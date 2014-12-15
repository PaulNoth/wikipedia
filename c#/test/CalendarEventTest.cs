using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Xml.Linq;
using WikiCalendar;

namespace WikiCalendarTest
{
	[TestClass]
	public class CalendarEventTest
	{

		[TestMethod]
		public void exportXML_test()
		{

			XElement expected = XElement.Load(@"..\..\..\..\Data\sample_output_enwiki-latest-pages-articles1_Test_example.xml");
			Console.WriteLine(expected.ToString());

			String inputPath =	@"..\..\..\..\Data\sample_input_enwiki-latest-pages-articles1.xml";
			String exportPath = @"..\..\..\..\Data\sample_output_enwiki-latest-pages-articles1_unit_test.xml";
			
			MyXMLParser control = new MyXMLParser();
			control.initParsing(inputPath);
			control.exportEventsXML(exportPath);

			Console.WriteLine(control.getXmlString());
			Assert.AreEqual(expected.ToString(), control.getXmlString());
			//XElement actual = testClass.exportXML();
		}
	}
}
