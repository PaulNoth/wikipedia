package wikical.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import wikical.dataclasses.DeathBirthParsedData;
import wikical.settings.Settings;

public class WikiParser {

	File sourceFilePath;
	FileSave fileSavePeople;											// output file with people names, dates of birth and death
	FileSave fileSaveEvents;											// output file with events, things, universities, ...
	String lastTitle = "";

	/**
	 * Will parse file from big source Wikipedia XML (sourceFilePath) and will create two files of People and Events
	 * @param sourceFilePath
	 */
	public WikiParser(File sourceFilePath) {
		this.sourceFilePath = sourceFilePath;

		fileSavePeople = new FileSave(sourceFilePath.getParent() + Settings.nameOfPeopleFile);		// output is saved where input is located
		fileSaveEvents = new FileSave(sourceFilePath.getParent() + Settings.nameOfEventsFile);
		
		try {
			execute(sourceFilePath.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executing of creating files
	 * @param xmlFileName
	 * @throws Exception
	 */
	private void execute(String xmlFileName) throws Exception {

		long startTime = System.currentTimeMillis();

		InputStream xmlInputStream = new FileInputStream(xmlFileName);
		XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
		XMLStreamReader2 xmlStreamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(xmlInputStream);
		while (xmlStreamReader.hasNext()) {
			int eventType = xmlStreamReader.next();
			switch (eventType) {
			case XMLEvent.START_ELEMENT:

				isStartElement(xmlStreamReader);

				break;
			default:
				break;
			}

		}

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Operation was successfull. Time of generating output was " + totalTime + " [ms]");

		fileSavePeople.stopWritingToFile();
		fileSaveEvents.stopWritingToFile();
	}

	/**
	 * Called when while parsing XML file start element is found
	 * 
	 * @param xmlStreamReader
	 * @throws Exception
	 */
	private void isStartElement(XMLStreamReader2 xmlStreamReader) throws Exception {

		String currentStartTag = xmlStreamReader.getLocalName();

		/* persons */
		String foundName = null;
		String rawBirthDate = null;
		String rawDeathDate = null;

		/* titles */
		String foundTitle = null;
		String rawStartDate = null;
		String rawEndDate = null;

		if (currentStartTag.equals("title")) {
			xmlStreamReader.next();
			lastTitle = xmlStreamReader.getText();
		} else {

			/* Every person processing starts here (starts for every page) */
			if (currentStartTag.equals("text") && xmlStreamReader.hasNext()) {				// when start tag "text is found" (which contains all the needed data)...

				String wholeText = "";

				while ((xmlStreamReader.next()) == XMLEvent.CHARACTERS) {					// .getText() return text in smaller parts, this reads it all

					if (xmlStreamReader.hasText()) {										// sometimes there is no text in "text" field
						wholeText = wholeText + xmlStreamReader.getText();					// before it was getText - problems with short output!; getElementText
					}
				}

				BufferedReader reader = new BufferedReader(new StringReader(wholeText));
				String line;

				while ((line = reader.readLine()) != null) {

					/* Parsing people */
					if (line.matches("^ *\\| *[Mm]eno *=.*") || line.matches("^ *\\| *[Pp]lné [Mm]eno *=.*")) {

						String foundSubstring = line.replaceAll(".*?=[\\[ ']*([\\p{L}0-9|'. ()–]+[\\p{L}.)]).*", "$1");

						/* it have to be null so it doesn't owerwrite good name with character from book */
						if (!foundSubstring.equals("") && !foundSubstring.equals(" ") && foundName == null) {
							if (foundSubstring.contains("{{PAGENAME}}") || foundSubstring.matches(".*[Mmeno].*")) {
								foundName = lastTitle;
							} else {
								foundName = foundSubstring;
							}
						}

					} else if (line.matches("^ *\\| *[Dd]átum narodenia *=.*") || line.matches("^ *\\| *[Nn]arodenie *=.*") || line.matches("^ *\\| *[Dd]átum a miesto narodenia  *=.*")) {
						Pattern pattern = Pattern.compile("= *(.*?)$");
						Matcher matcher = pattern.matcher(line);
						if (matcher.find()) {
							String birthDate = matcher.group(1);
							if (!birthDate.equals("") && !birthDate.equals(" ")) {
								rawBirthDate = birthDate;
							}
						}
					} else if (line.matches("^ *\\| *[Dd]átum úmrtia *=.*") || line.matches("^ *\\| *[Úú]mrtie *=.*") || line.matches("^ *\\| *[Dd]átum a miesto úmrtia *=.*")) {
						Pattern pattern = Pattern.compile("= *(.*?)$");
						Matcher matcher = pattern.matcher(line);
						if (matcher.find()) {
							String deathDate = matcher.group(1);
							if (!deathDate.equals("") && !deathDate.equals(" ")) {
								rawDeathDate = deathDate;
							}
						}
					}

					
					/* Parsing "zalozenie, zatvorenie" (e.g. STU) */

					else if (line.matches("^ *\\| *[Nn][aá]zov *=.*")) {

						Pattern pattern = Pattern.compile("= *(.*?)$");
						Matcher matcher = pattern.matcher(line);
						if (matcher.find()) {
							String foundSubstring = matcher.group(1);

						//String foundSubstring = line.replaceAll(".*?=[\\[ ']*([\\p{L}0-9|'. ()]+[\\p{L}.)]).*", "$1");
						
							/* it have to be null so it doesn't owerwrite good title */
							if (!foundSubstring.equals("") && !foundSubstring.equals(" ") && foundTitle == null) {
								if (foundSubstring.contains("{{PAGENAME}}") || foundSubstring.matches(".*[Nn][aá]zov.*")) {
									foundTitle = lastTitle;
								} else {
									foundTitle = foundSubstring;
								}
								
								//System.out.println("title: " + foundTitle);
							}
						}
					}
					else if (line.matches("^ *\\| *[Rr]ok zalo[zž]enia *=.*") || line.matches("^ *\\| *[Pp]rv[aá] zmienka *=.*")) {	// | Prvá zmienka

						Pattern pattern = Pattern.compile("= *(.*?)$");
						Matcher matcher = pattern.matcher(line);
						if (matcher.find()) {
							String foundSubstring = matcher.group(1);

							if (!foundSubstring.equals("") && !foundSubstring.equals(" ")) {
								rawStartDate = foundSubstring;
								//System.out.println("date: " + foundSubstring);
							}
						}
					}					
				}
			}
		}

		/* when the name is found - we can try parsing dates */

		if (foundName != null) {															// if name is found
			
			DeathBirthParsedData parsedData = null;

			if (rawDeathDate != null && !rawDeathDate.contains("<!--")) {					//if it is no commented code
				parsedData = DateParser.parseDeathDate(rawDeathDate);							// it could
			}

			if (parsedData == null) {
				parsedData = new DeathBirthParsedData();								// when parsedData returns null (avoiding nullpointerexception)
			}

			/* When deathDate found just date of death and not birth date */
			if (parsedData.birthDate == null && rawBirthDate != null) {

				/* When birth date wasn't found from date of death */
				String birthParsed = DateParser.parseSimpleTime(rawBirthDate);
				if (birthParsed != null) {
					parsedData.birthDate = birthParsed;
				}
			}

			if (parsedData != null && (parsedData.birthDate != null || parsedData.deathDate != null)) {
				writeToFile(parsedData, foundName);
			}
		}
		
		if (foundTitle != null && (rawStartDate != null)) {								//it's possible that foundTitle have also foundName (cannot be else if)
			String parsedStartDate = DateParser.parseSimpleTime(rawStartDate);
			if(parsedStartDate != null) {
				//System.out.println("parsed: " + foundTitle + " datum: " + parsedStartDate);
				fileSaveEvents.addLineToFile(foundTitle + ";" + parsedStartDate, true);
			}
		}
	}

	/**
	 * Now both dates are parsed and ready to be written (if they are not null)
	 */
	private void writeToFile(DeathBirthParsedData parsedData, String foundName) {

		fileSavePeople.addLineToFile(foundName, true);								// only names with at least one date will be saved

		if (parsedData.birthDate != null) {
			fileSavePeople.addLineToFile(";" + parsedData.birthDate, false);

		} else {
			fileSavePeople.addLineToFile(";", false);
		}

		if (parsedData.deathDate != null) {
			fileSavePeople.addLineToFile(";" + parsedData.deathDate, false);
		}

	}
}
