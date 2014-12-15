/**
 * Interface for working with main window.
 */
public class WindowInterface {

	private String directoryPath;
	Parser p;
	FileParser fp;
	PageRankReader prr;
	
	WindowInterface()
	{
		directoryPath = null;
		p = new Parser();
		fp = new FileParser();
		prr = new PageRankReader();
	}
	public void setPath(String _path)
	{
		directoryPath = _path;
	}
	
	public String getPath()
	{
		return directoryPath;
	}
	public void parsePageFile()
	{
		fp.parseFile(directoryPath);
	}
	public void parseLinksfile()
	{
		p.readFile(directoryPath);
	}
	public void countPageRank()
	{
		p.getPageRank();
	}
	public String getPageRank(String _odkaz)
	{
		prr.setPath(directoryPath);
		return prr.getPageRank(_odkaz);
	}
}
