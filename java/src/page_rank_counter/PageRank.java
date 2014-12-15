import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
/**
 * class for counting the pagerank.
 *
 */

public class PageRank {
	
	private double d;
	PageRank()
	{
		d = 0.85; //tlmiaci faktor - moze byt akakolvek hodnota, nastavene na 0.85 podla strany 45 v skriptach
	}
	
	public double countNext(double pr, double pl, double actualPageRank) //na spocitanie sumy vsetkych stranok 
	{
		return (actualPageRank + (pr/pl));
	}
	
	public double getPageRank(double e)
	{
		return countPageRank(e);
	}
	
	public void setDampingFactor (double _d)
	{
		d = _d;
	}
	private double countPageRank(double sum)
	{
		return ((1-d)+(d*sum));  //sum je suma vsetkych pagerankov / poctom odchadzajucich linkov pocita sa hore
	}
}
