package Parser;

import java.io.Serializable;

//trieda predstavuje infobox settlement so vsetkymi atributmi, obsahuje iba atributy a funkcie get, set !
public class Infobox_settlement implements Serializable {
	
	private String official_name;
	private String nickname;
	private String map_caption;
	private String coordinates_region;
	private String leader_title;
	private String unit_pref;
	private String area_total_km2;
	private String area_land_km2;
	private String population_total;
	private String population_density_km2;
	private String timezone;
	private String website;
	private String[] postal_code;
	
	public String getOfficial_name() {
		return official_name;
	}
	
	public void setOfficial_name(String official_name) {
		this.official_name = official_name;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getMap_caption() {
		return map_caption;
	}
	
	public void setMap_caption(String map_caption) {
		this.map_caption = map_caption;
	}
	
	public String getCoordinates_region() {
		return coordinates_region;
	}
	
	public void setCoordinates_region(String coordinates_region) {
		this.coordinates_region = coordinates_region;
	}
	
	public String getLeader_title() {
		return leader_title;
	}
	
	public void setLeader_title(String leader_title) {
		this.leader_title = leader_title;
	}
	
	public String getUnit_pref() {
		return unit_pref;
	}
	
	public void setUnit_pref(String unit_pref) {
		this.unit_pref = unit_pref;
	}
	
	public String getArea_total_km2() {
		return area_total_km2;
	}
	
	public void setArea_total_km2(String area_total_km2) {
		this.area_total_km2 = area_total_km2;
	}
	
	public String getArea_land_km2() {
		return area_land_km2;
	}
	
	public void setArea_land_km2(String area_land_km2) {
		this.area_land_km2 = area_land_km2;
	}
	
	public String getPopulation_total() {
		return population_total;
	}
	
	public void setPopulation_total(String population_total) {
		this.population_total = population_total;
	}
	
	public String getPopulation_density_km2() {
		return population_density_km2;
	}
	
	public void setPopulation_density_km2(String population_density_km2) {
		this.population_density_km2 = population_density_km2;
	}
	
	public String getTimezone() {
		return timezone;
	}
	
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public String[] getPostal_code() {
		return postal_code;
	}
	
	public void setPostal_code(String[] postal_code) {
		this.postal_code = postal_code;
	}
	
	
	
}
