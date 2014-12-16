package wikical.datacompare;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wikical.dataclasses.SearchData;

/**
 * 
 * @author Martin Polak
 *
 */
public class DataCompare {
	
	/**
	 * Compare two dates and return if they overlap
	 * @return - true if they overlap, false if they are not
	 */
	public static boolean CouldTheyMeet(SearchData searchData, SearchData selectedItem) {

			Date firstDateDeath = returnDate(selectedItem.getDeathDate());
			Date secondDateBirth = returnDate(searchData.getBirthDate());
			
			Date firstDateBirth = returnDate(selectedItem.getBirthDate());
			Date secondDateDeath = returnDate(searchData.getDeathDate());
			
			int comparedDates1 = 0;
			int comparedDates2 = 0;
			
			if(firstDateDeath == null && secondDateDeath == null) {
				return true;																//oba konce su aktualne
			}
			
			if(firstDateDeath != null && secondDateBirth != null) {							//ak su zadane oba datumy
				comparedDates1 = firstDateDeath.compareTo(secondDateBirth);
			}
			else {
				if(firstDateBirth.compareTo(secondDateDeath) >= 0) {						//datum narodenia je vacsi ako datum umrtia - nestretli sa
					return false;
				} else {
					return true;
				}
			}		
			
			if(firstDateBirth != null && secondDateDeath != null) {							//ak su zadane oba datumy
				comparedDates2 = firstDateBirth.compareTo(secondDateDeath);
			}
			else {
				if(secondDateBirth.compareTo(firstDateDeath) >= 0) {						//datum narodenia je vacsi ako datum umrtia - nestretli sa
					return false;
				} else {
					return true;
				}
			}			
			
			
			if(comparedDates1 >= 0 && comparedDates2 <= 0) {		// (datum konca 1. je vacsi ako datum zaciatku 2.) (datum zaciatku 1. je mensi ako datum konca 2.)
				System.out.println("YES. He did.");
				return true;
			}

		
		return false;
	}
	
	/**
	 * Convert string to date
	 * @param input - it can be in format with day, month and year or just year
	 */
	private static Date returnDate(String input) {
				
		try {
			if(input.equals("")) {
				return null;
			}
			
			DateFormat dateFormat = new SimpleDateFormat("d.M.y");
			DateFormat dateFormatYear = new SimpleDateFormat("..y");
			
			if(input.contains("..")) {
				return dateFormatYear.parse(input);
			} else {
				return dateFormat.parse(input);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
