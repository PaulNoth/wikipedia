using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WikiCalendar
{
	/// <summary>
	/// Collection of CalendarEvents which happened at same date.
	/// </summary>
	class DayEventCollection:ICollection<CalendarEvent>
	{
		DateTime date;
		public int day {get; set; }
		public int month { get; set; }
		public int year { get; set; }


		HashSet<CalendarEvent> eventList {get; set;}

		public DayEventCollection(int _day,int _month, int _year,CalendarEvent cEvent) 
		{
			day = _day;
			month = _month;
			year = _year;
			
			
			eventList = new HashSet<CalendarEvent>() { cEvent };
		}

		public HashSet<CalendarEvent> getEventsArray()
		{
			return eventList;
		}

		public void Add(CalendarEvent item)
		{
			var x = from e in eventList
					where e.id != item.id 
					where e.dateId == item.dateId
					select e;

			if (x.Any()) 
			{
				eventList.Add(item);
			}
			
		}

		public void Clear()
		{
			eventList.Clear();
			throw new NotImplementedException();
		}

		public bool Contains(CalendarEvent item)
		{
			throw new NotImplementedException();
		}

		public void CopyTo(CalendarEvent[] array, int arrayIndex)
		{
			throw new NotImplementedException();
		}

		public int Count
		{
			get { throw new NotImplementedException(); }
		}

		public bool IsReadOnly
		{
			get { throw new NotImplementedException(); }
		}

		public bool Remove(CalendarEvent item)
		{
			throw new NotImplementedException();
		}

		public IEnumerator<CalendarEvent> GetEnumerator()
		{
			throw new NotImplementedException();
		}

		System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
		{
			throw new NotImplementedException();
		}
	}
}
