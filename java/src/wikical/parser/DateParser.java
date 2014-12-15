package wikical.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wikical.dataclasses.DeathBirthParsedData;

public class DateParser {

	/**
	 * from raw death date will create death and birth date (if it contains birth date) in DeathBirthParsedData class
	 */
	public static DeathBirthParsedData parseDeathDate(String sourceDate) {
		
		if ((sourceDate.matches(".*d[uú]v\\|[0-9].*"))) {
			
			if(sourceDate.contains("RRRR")) {
				System.out.println("");
			}
			
			Pattern pattern = Pattern.compile("\\{\\{d[uú]v\\|([0-9]+.*?)\\}\\}");
			String foundSubstring = "";

			Matcher matcher = pattern.matcher(sourceDate);
			if (matcher.find()) {
				foundSubstring = matcher.group(1);

				String[] tokens = foundSubstring.split("\\|");

				if (tokens.length > 5) {

					return new DeathBirthParsedData(tokens[5] + "." + tokens[4] + "." + tokens[3], tokens[2] + "." + tokens[1] + "." + tokens[0]);
				} else {
					return null;
				}
			}
		} else {
			
			return new DeathBirthParsedData(null, parseSimpleTime(sourceDate));
		}
		return null;
	}

	/**
	 * Will work for simple time (not when there is date of birth and death in one string)
	 * @param sourceDate
	 * @return
	 */
	public static String parseSimpleTime(String sourceDate) {
		
		if (sourceDate.matches(".*dnv\\|[0-9].*")) {									// {{dnv|1941|2|19}}, returns just date of birth

			Pattern pattern = Pattern.compile("\\{\\{dnv\\|(.*?)\\}\\}");
			String foundSubstring = "";

			Matcher matcher = pattern.matcher(sourceDate);
			if (matcher.find()) {
				foundSubstring = matcher.group(1);

				String[] tokens = foundSubstring.split("\\|");

				if (tokens.length > 2) {

					return new String(tokens[2] + "." + tokens[1] + "." + tokens[0]);
				} else {
					return null;
				}
			}
		} else if (sourceDate.matches("\\[\\[[0-9].*\\]\\] \\[\\[[0-9]*\\]\\].*")) {		// [[23. január]] [[1840]]

			String foundSubstringDay = null;
			String foundSubstringMonth = SlovakMonthParser.getMonthNumber(sourceDate);
			String foundSubstringYear = "";

			/* Date and month search */
			Pattern pattern = Pattern.compile(".*\\[\\[([0-9]{1,2})\\.");
			Matcher matcher = pattern.matcher(sourceDate);
			if (matcher.find()) {
				foundSubstringDay = matcher.group(1);
			}

			/* Year search */
			pattern = Pattern.compile(".*\\[\\[([0-9]{1,4})\\]\\]");

			matcher = pattern.matcher(sourceDate);
			if (matcher.find()) {
				foundSubstringYear = matcher.group(1);
			}
			/* transform [[11. september]] [[1063]] -> 11.9.1063 */

			if (foundSubstringDay != null && foundSubstringMonth != null && foundSubstringYear != null) {
				return new String(foundSubstringDay + "." + foundSubstringMonth + "." + foundSubstringYear);
			} else if (foundSubstringMonth != null && foundSubstringYear != null) {
				return new String("." + foundSubstringMonth + "." + foundSubstringYear);
			} else if (foundSubstringYear != null) {
				return new String("." + "." + foundSubstringYear);
			}
		}
		else if(sourceDate.matches(" *\\[\\[[0-9]*\\]\\].*")) {					//just year
			/* Year search */
			Pattern pattern = Pattern.compile(" *\\[\\[([0-9]{1,4})\\]\\]");

			Matcher matcher = pattern.matcher(sourceDate);
			if (matcher.find()) {
				return new String("." + "." + matcher.group(1));
			}
		}
		else if(sourceDate.matches(" *[0-9]{1,4} *")) {					//just year
			/* Year search */
			Pattern pattern = Pattern.compile(" *([0-9]{1,4}) *");

			Matcher matcher = pattern.matcher(sourceDate);
			if (matcher.find()) {
				return new String("." + "." + matcher.group(1));
			}
		}

		return null;
	}

}
