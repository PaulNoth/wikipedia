package Parser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;


//Vypocitanie statistik o pocte vyskytov atributov
public class Stats {

	public void stats_book (List<Infobox_book> InfoboxbookList, PrintWriter out_stats){
		//inicializacia
		int velkost = InfoboxbookList.size();
		int pocet_mien = 0;
		int pocet_translator = 0;
		int pocet_image = 0;
		int pocet_captation = 0;
		int pocet_author = 0;
		int pocet_country = 0;
		int pocet_language = 0;
		int pocet_subject = 0;
		int pocet_genre = 0;
		int pocet_published = 0;
		int pocet_pages = 0;
		int pocet_isbn = 0;
		int pocet_followed_by = 0;
		int pocet_precessed_by = 0;
		
		//zistim si pocetnost atributov v oparsovanych datach
		for(Infobox_book book : InfoboxbookList){

			if (book.getName() != null) {
        		pocet_mien++;
        	}
        	
        	if (book.getTranslator() != null) {
        		pocet_translator++;
        	}
			
        	if (book.getImage() != null) {
        		pocet_image++;
        	}
        	
        	if (book.getCaption() != null) {
        		pocet_captation++;
        	}
        	
        	if (book.getAuthor() != null) {
        		pocet_author++;
        	}
        	
        	if (book.getCountry() != null) {
        		pocet_country++;
        	}
					
			if (book.getLanguage() != null){
				pocet_language++;
			}	
			
			if (book.getSubject() != null){
				pocet_subject++;
			}
					
			if (book.getGenre() != null) {
				pocet_genre++;
			}
			
			if (book.getPublished() != null) {
				pocet_published++;
			}
			
			
			if (book.getPages() != null) {
				pocet_pages++;
			}
			if (book.getIsbn() != null) {
				pocet_isbn++;
			}
			if (book.getFollowed_by() != null) {
				pocet_followed_by++;
			}
			if (book.getPreceded_by() != null) {
				pocet_precessed_by++;
			}		
		}
		
		//vypis
		out_stats.println("Statistiky book: ");
		out_stats.println("Percento nevyparsovanych mien knih: " + prepocet(velkost, pocet_mien));
		out_stats.println("Percento nevyparsovanych translatorov: " + prepocet(velkost, pocet_translator));
		out_stats.println("Percento nevyparsovanych image: " + prepocet(velkost, pocet_image));
		out_stats.println("Percento nevyparsovanych captation: " + prepocet(velkost, pocet_captation));
		out_stats.println("Percento nevyparsovanych autorov: " + prepocet(velkost, pocet_author));
		out_stats.println("Percento nevyparsovanych country: " + prepocet(velkost, pocet_country));
		out_stats.println("Percento nevyparsovanych languages: " + prepocet(velkost, pocet_language));
		out_stats.println("Percento nevyparsovanych subject: " + prepocet(velkost, pocet_subject));
		out_stats.println("Percento nevyparsovanych genre: " + prepocet(velkost, pocet_genre));
		out_stats.println("Percento nevyparsovanych published: " + prepocet(velkost, pocet_published));
		out_stats.println("Percento nevyparsovanych pages: " + prepocet(velkost, pocet_pages));
		out_stats.println("Percento nevyparsovanych isbn: " + prepocet(velkost, pocet_isbn));
		out_stats.println("Percento nevyparsovanych followed_by: " + prepocet(velkost, pocet_followed_by));
		out_stats.println("Percento nevyparsovanych precessed_by: " + prepocet(velkost, pocet_precessed_by));
		out_stats.println("********************************************************************");
		
	}

	public void stats_country ( List<Infobox_country> InfoboxList, PrintWriter out_stats){
		//inicializacia
		int velkost = InfoboxList.size();
		int pocet_mien = 0;
		int pocet_common_name = 0;
		int pocet_image_flag = 0;
		int pocet_image_coat = 0;
		int pocet_capital = 0;
		int pocet_official_religion = 0;
		int pocet_official_languages = 0;
		int pocet_government_type = 0;
		int pocet_Area_km2 = 0;
		int pocet_Area_sq_mi = 0;
		int pocet_Currency = 0;
		int pocet_Currency_code = 0;
		int pocet_Population_estimate = 0;
		int pocet_Population_estimate_rank = 0;
	
		//zistim si pocetnost atributov v oparsovanych datach
		for(Infobox_country country : InfoboxList){

			if (country.getTitle() != null) {
        		pocet_mien++;
        	}
        	
        	if (country.getCommon_name() != null) {
        		pocet_common_name++;
        	}
			
        	if (country.getImage_flag() != null) {
        		pocet_image_flag++;
        	}
        	
        	if (country.getImage_coat() != null) {
        		pocet_image_coat++;
        	}
        	
        	if (country.getCapital() != null) {
        		pocet_capital++;
        	}
					
			if (country.getOfficial_religion() != null){
				pocet_official_religion++;
			}	
			
			if (country.getOfficial_languages() != null){
				pocet_official_languages++;
			}
					
			if (country.getGovernment_type() != null) {
				pocet_government_type++;
			}
			
			if (country.getArea_km2() != null) {
				pocet_Area_km2++;
			}
			
			if (country.getArea_sq_mi() != null) {
				pocet_Area_sq_mi++;
			}
			if (country.getCurrency() != null) {
				pocet_Currency++;
			}
			if (country.getCurrency_code() != null) {
				pocet_Currency_code++;
			}
			if (country.getPopulation_estimate() != null) {
				pocet_Population_estimate++;
			}
			if (country.getPopulation_estimate_rank() != null) {
				pocet_Population_estimate_rank++;
			}		
		}
		
		//vypis
		out_stats.println("Statistiky country: ");
		out_stats.println("Percento nevyparsovanych mien: " + prepocet(velkost, pocet_mien));
		out_stats.println("Percento nevyparsovanych common_name: " + prepocet(velkost, pocet_common_name));
		out_stats.println("Percento nevyparsovanych image_flag: " + prepocet(velkost, pocet_image_flag));
		out_stats.println("Percento nevyparsovanych image_coat: " + prepocet(velkost, pocet_image_coat));
		out_stats.println("Percento nevyparsovanych capital: " + prepocet(velkost, pocet_capital));
		out_stats.println("Percento nevyparsovanych official_religion: " + prepocet(velkost, pocet_official_religion));
		out_stats.println("Percento nevyparsovanych official_languages: " + prepocet(velkost, pocet_official_languages));
		out_stats.println("Percento nevyparsovanych goverment_type: " + prepocet(velkost, pocet_government_type));
		out_stats.println("Percento nevyparsovanych area_km2: " + prepocet(velkost, pocet_Area_km2));
		out_stats.println("Percento nevyparsovanych area_sq_mi: " + prepocet(velkost, pocet_Area_sq_mi));
		out_stats.println("Percento nevyparsovanych currency: " + prepocet(velkost, pocet_Currency));
		out_stats.println("Percento nevyparsovanych currency_code: " + prepocet(velkost, pocet_Currency_code));
		out_stats.println("Percento nevyparsovanych population_estimate: " + prepocet(velkost, pocet_Population_estimate));
		out_stats.println("Percento nevyparsovanych population_estimate_rank: " + prepocet(velkost, pocet_Population_estimate_rank));
		out_stats.println("********************************************************************");
	}
	
	public void stats_person ( List<Infobox_person> InfoboxPersonList, PrintWriter out_stats){
		//inicializacia
		int velkost = InfoboxPersonList.size();
		int pocet_mien = 0;
		int pocet_obrazkov = 0;
		int pocet_obrazkov_s_velkostou = 0;
		int pocet_narodeni = 0;
		int pocet_narodeni_mesto = 0;
		int pocet_datumov_smrti = 0;
		int pocet_datumov_smrti_mesto = 0;
		int pocet_occupation = 0;
		
		//zistim si pocetnost atributov v oparsovanych datach
		for(Infobox_person person : InfoboxPersonList){
        	if (person.getName() != null) {
        		pocet_mien++;
        	}
        	
        	if (person.getImage() != null) {
        		pocet_obrazkov++;
        	}
			
        	if (person.getImage_size() != null) {
        		pocet_obrazkov_s_velkostou++;
        	}
        	
        	if (person.getBirth_date() != null) {
        		pocet_narodeni++;
        	}
					
			if (person.getBirth_place() != null){
				pocet_narodeni_mesto++;
			}		
					
			if (person.getDeath_date() != null) {
				pocet_datumov_smrti++;
			}
			
			if (person.getDeath_place() != null) {
				pocet_datumov_smrti_mesto++;
			}
			
				
        }
		
		//vypis
		out_stats.println("Statistiky person: ");
		out_stats.println("Percento nevyparsovanych mien: " + prepocet(velkost, pocet_mien));
		out_stats.println("Percento nevyparsovanych obrazkov: " + prepocet(velkost, pocet_obrazkov));
		out_stats.println("Percento nevyparsovanych velkosti obrazkov: " + prepocet(velkost, pocet_obrazkov_s_velkostou));
		out_stats.println("Percento nevyparsovanych dni narodeni: " + prepocet(velkost, pocet_narodeni));
		out_stats.println("Percento nevyparsovanych miest narodeni: " + prepocet(velkost, pocet_narodeni_mesto));
		out_stats.println("Percento nevyparsovanych datumov umrti: " + prepocet(velkost, pocet_datumov_smrti));
		out_stats.println("Percento nevyparsovanych miest umrti: " + prepocet(velkost, pocet_datumov_smrti_mesto));
		
		out_stats.println("********************************************************************");
	}

	public void stats_settlement (List<Infobox_settlement> InfoboxSettlementList, PrintWriter out_stats){
		
		//inicializacia
		int velkost = InfoboxSettlementList.size();
		int pocet_mien = 0;
		int pocet_nickname = 0;
		int pocet_captation = 0;
		int pocet_coordinates_region = 0;
		int pocet_leader_titulov = 0;
		int pocet_unit_pref = 0;
		int pocet_area_total_km = 0;
		int pocet_area_land_km = 0;
		
		int pocet_population = 0;
		int pocet_population_density = 0;
		int pocet_timezone = 0;
		int pocet_website = 0;
		int pocet_postal_code = 0;
		
		//zistim si pocetnost atributov v oparsovanych datach
		for(Infobox_settlement sett : InfoboxSettlementList){

			if (sett.getOfficial_name() != null) {
        		pocet_mien++;
        	}
        	
        	if (sett.getNickname() != null) {
        		pocet_nickname++;
        	}
			
        	if (sett.getMap_caption() != null) {
        		pocet_captation++;
        	}
        	
        	if (sett.getCoordinates_region() != null) {
        		pocet_coordinates_region++;
        	}
					
			if (sett.getLeader_title() != null){
				pocet_leader_titulov++;
			}		
					
			if (sett.getUnit_pref() != null) {
				pocet_unit_pref++;
			}
			
			if (sett.getArea_total_km2() != null) {
				pocet_area_total_km++;
			}
			
			if (sett.getArea_land_km2() != null) {
				pocet_area_land_km++;
			}
			if (sett.getPopulation_total() != null) {
				pocet_population++;
			}
			if (sett.getPopulation_density_km2() != null) {
				pocet_population_density++;
			}
			if (sett.getTimezone() != null) {
				pocet_timezone++;
			}
			if (sett.getWebsite() != null) {
				pocet_website++;
			}
			if (sett.getPostal_code() != null) {
				pocet_postal_code++;
			}
        }
		
		//vypis
		out_stats.println("Statistiky settlement: ");
		out_stats.println("Percento nevyparsovanych mien: " + prepocet(velkost, pocet_mien));
		out_stats.println("Percento nevyparsovanych nickname: " + prepocet(velkost, pocet_nickname));
		out_stats.println("Percento nevyparsovanych captation: " + prepocet(velkost, pocet_captation));
		out_stats.println("Percento nevyparsovanych coordinates_region: " + prepocet(velkost, pocet_coordinates_region));
		out_stats.println("Percento nevyparsovanych leader titulov: " + prepocet(velkost, pocet_leader_titulov));
		out_stats.println("Percento nevyparsovanych unit_pref: " + prepocet(velkost, pocet_unit_pref));
		out_stats.println("Percento nevyparsovanych area_total_km2: " + prepocet(velkost, pocet_area_total_km));
		out_stats.println("Percento nevyparsovanych area_land_km2: " + prepocet(velkost, pocet_area_land_km));
		out_stats.println("Percento nevyparsovanych populacii: " + prepocet(velkost, pocet_population));
		out_stats.println("Percento nevyparsovanych population_density: " + prepocet(velkost, pocet_population_density));
		out_stats.println("Percento nevyparsovanych timezone: " + prepocet(velkost, pocet_timezone));
		out_stats.println("Percento nevyparsovanych website: " + prepocet(velkost, pocet_website));
		out_stats.println("Percento nevyparsovanych postal_code: " + prepocet(velkost, pocet_postal_code));
		out_stats.println("********************************************************************");
		
	}
	
	public double prepocet(int velkost, int pocet){
		//vypocet na zaklade spocitanych hodnot
		double rozdiel = velkost - pocet;
		double a = 100.0/velkost;
		return a*rozdiel;
	}
	
	public void vypocitaj_statistiky(List<Infobox_book> InfoboxbookList, List<Infobox_country> InfoboxList, List<Infobox_settlement> InfoboxSettlementList, List<Infobox_person> InfoboxPersonList) throws FileNotFoundException{
		//Zapisanie do suboru
		PrintWriter out_stats = new PrintWriter("Stats.txt");
		stats_book(InfoboxbookList, out_stats);
		stats_country(InfoboxList, out_stats);
		stats_person(InfoboxPersonList, out_stats);
		stats_settlement(InfoboxSettlementList, out_stats);
		out_stats.close();
	}
}
