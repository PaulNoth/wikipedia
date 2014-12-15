package anchors;

import java.util.ArrayList;
import java.util.List;

public class Record_list {

	private List<Record> list_of_records = new ArrayList<Record>();
	
	//method add new record to the List of records
	void add_record(String page_title, String page_link, String page_anchor)
	{
		Record r = new Record();
		
		r.set_page_title(page_title);
		r.set_page_link(page_link);
		r.set_page_anchor(page_anchor);
		
		list_of_records.add(r);
	}
	
	
	//method prints all records from list of records to console
	void print_list()
	{	for(Record e: list_of_records)
		{	System.out.println(e.return_page_title() + "\t\t " + e.return_page_link() + "\t\t" + e.return_page_anchor());	}		
	}
	
	
	public List<Record>return_r()
	{	return list_of_records;	}
	
}
