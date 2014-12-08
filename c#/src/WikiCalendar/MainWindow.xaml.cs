using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Xml.Linq;
using System.Text.RegularExpressions;
using System.Threading;

namespace WikiCalendar
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
		MyXMLParser control;
        public MainWindow()
        {
            InitializeComponent();
			control = new MyXMLParser();
        }
		private void Button_Click(object sender, RoutedEventArgs e)
		{
			String inputPath =
				//@"..\..\..\..\Data\Aristotle.txt";
				//@"D:\downNew\wiki\enwiki-latest-pages-articles1.xml-p000000010p000010000";
			//@"D:\downNew\wiki\enwiki-latest-pages-articles1.xml-p000000010p000010000";
			//		@"..\..\..\..\Data\sample_input_enwiki-latest-pages-articles1.xml";
			//@"D:\downNew\wiki\enwiki-latest-pages-articles6.xml";
			@"D:\downNew\wiki\enwiki-latest-pages-articles3.xml-p000025001p000055000";
			//@"D:\downNew\wiki\enwiki-latest-pages-articles10.xml-p000925001p001325000";//1gb
			//@"D:\downNew\wiki\enwiki-latest-pages-articles22.xml-p015725013p018225000"; //privelky
			String exportPath = @"..\..\..\..\Data\sample_output_enwiki-latest-pages-articles1_Real_output.xml";

			//Thread backgroundThread = new Thread(
			//	new ThreadStart(() =>
			//	{
			//		for (int n = 0; n < 1000; n++)
			//		{
			//			if (control.pagesCount != 0)
			//			{
			//				Thread.Sleep(50);
			//				pagesProgresBar.Value = control.pageCounter / control.pagesCount;
			//				pagesCountTextBox.Text = control.pagesCount.ToString();
			//				eventsCountTextBox.Text = control.eventsCount.ToString();
			//			}
						
			//		}

			//		MessageBox.Show("Thread completed!");
			//		pagesProgresBar. Value = 100;
			//	}
			//));


			//backgroundThread.Start();

			control.initParsing(inputPath);
			control.exportEventsXML(exportPath);
			List<long> days = control.getAllDays();
			foreach (long day in days)
			{
				DropDownMenuDate.Items.Add(day);
			}
			List<String> distinctEvents = control.getDistinctEventTypes();
			foreach (String dEvent in distinctEvents)
			{
				TextConsole.Text += dEvent + '\n'; 
			}
			
			pagesCountTextBox.Text = control.pagesCount.ToString();
			eventsCountTextBox.Text = control.eventsCount.ToString();

			statistics.Text = control.getEventStatistics();



		}

		private void dateSearchTextBox_TextChanged(object sender, TextChangedEventArgs e)
		{
			




		}

		private void Button_Click_1(object sender, RoutedEventArgs e)
		{
			TextConsole.Text = "";
			HashSet<CalendarEvent> resultSet;

			if(dateSearchTextBox.Text.Equals(""))
			{
				resultSet = control.searchDayEvents(DropDownMenuDate.SelectedValue.ToString());
			}
			else 
			{
				resultSet = control.searchDayEvents(dateSearchTextBox.Text);
			
			}
			
			TextConsole.Text += "\n\nDatekey:" + dateSearchTextBox.Text;
			if (resultSet == null)
			{
				return;
			}
			foreach (CalendarEvent ce in resultSet)
			{
				TextConsole.Text += "\n\t" +ce.dateId +"->" + ce.title + ":" + ce.eventType + "\n";
			}


		}

		private void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			Button_Click_1(sender, e);
			
		}

		private void calendarSelection_SelectedDatesChanged(object sender, SelectionChangedEventArgs e)
		{
			TextConsole.Text = "";
			HashSet<CalendarEvent> resultSet;

			var date = calendarSelection.SelectedDate.Value;
			resultSet= control.searchDayEvents((date.Year * 10000 + date.Month * 100 + date.Day).ToString());

			TextConsole.Text += "\n\nDatekey:" + dateSearchTextBox.Text;
			if (resultSet == null)
			{
				return;
			}
			foreach (CalendarEvent ce in resultSet)
			{
				TextConsole.Text += "\n\t" + ce.dateId + "->" + ce.title + ":" + ce.eventType + "\n";
			}
		}



	
    }
}
