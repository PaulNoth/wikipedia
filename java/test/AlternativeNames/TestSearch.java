package Parser;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit Test na hladanie vyrazov vo vyparsovanom subore.
 * @author Samuel Benkovic
 *
 */
public class TestSearch {
	main mainclass = new main();
	JTextField searchExpression;
	JButton btnSearch;
	JTextArea ResultData;
	JComboBox<String> TagCombo;
	@Before 
	public void init(){
		searchExpression = new JTextField();
		mainclass.setGu(new GUI());
		mainclass.getGu().createGUI();
		searchExpression = mainclass.getGu().searchExpression;
		btnSearch = mainclass.getGu().btnSearch;
		ResultData= mainclass.getGu().ResultData;
		TagCombo = mainclass.getGu().TagCombo;
	}
	/**
	 * Otestovanie že sa niè nenájde
	 */
	@Test
	public void TestNoneresult() {
		searchExpression.setText("peknydenprajem");
		btnSearch.doClick();
		String result = ResultData.getText();
		String ExpectedResult = "Found 0 hits.";
		Assert.assertTrue(result.contains(ExpectedResult));
	}
	/**
	 * Test hladania title:blindness z dumbu enwiki-latest-pages-articles1.xml-p000000010p000010000
	 */
	@Test
	public void Test1result() {
		searchExpression.setText("blindness");
		btnSearch.doClick();
		String result = ResultData.getText();
		String ExpectedResult = "Found 1 hits.";
		Assert.assertTrue(result.contains(ExpectedResult));
	}
	/**
	 * Test hladania title:Ford z dumbu enwiki-latest-pages-articles10.xml-p000925001p001325000
	 */
	@Test
	public void TestMoreresult() {
		searchExpression.setText("ford");
		btnSearch.doClick();
		String result = ResultData.getText();
		String ExpectedResult = "Found 0 hits.";
		String ExpectedResult2 = "Found 1 hits.";
		Assert.assertTrue(!result.contains(ExpectedResult) && !result.contains(ExpectedResult2));
	}

	/**
	 * Test hladania konkrétneho title:"Ford Model N" 
	 * z dumbu enwiki-latest-pages-articles10.xml-p000925001p001325000
	 */
	@Test
	public void TestAkaresult() {
		searchExpression.setText("\"Ford Model N\"");
		btnSearch.doClick();
		String result = ResultData.getText();
		String ExpectedResult = "aka: Ford Model R Ford Model S";
		Assert.assertTrue(result.contains(ExpectedResult));
	}
	/**
	 * Test hladania konkrétneho nájdeného akaTextu pri Title:blindness 
	 * z dumbu enwiki-latest-pages-articles1.xml-p000000010p000010000
	 */
	@Test
	public void TestMoreAkaTextresult() {
		searchExpression.setText("blindness");
		btnSearch.doClick();
		String result = ResultData.getText();
		String [] resultSubstring = result.split("Title");
		Assert.assertTrue(resultSubstring.length==3);
	}
	/**
	 * Test hladania konkrétneho nájdeného akaTextTitle pri hladaní akaTextTitle:Docklands 
	 * z dumbu enwiki-latest-pages-articles1.xml-p000000010p000010000
	 */
	@Test
	public void TestSearchAkaText() {
		searchExpression.setText("Docklands");
		TagCombo.setSelectedIndex(2); // select akaTextTitle
		btnSearch.doClick();
		String result = ResultData.getText();
		String ExpectedResult = "Aarhus Docklands";
		Assert.assertTrue(result.contains(ExpectedResult));
	}
}

