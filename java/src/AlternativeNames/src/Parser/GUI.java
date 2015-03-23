package Parser;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Trieda venujúca sa hlavne GUI Vytvoreniu elementov a podobne
 * @author Samuel Benkovic
 *
 */
public class GUI extends JFrame {

	JPanel mainPanel,panelTop,panelBottomTop,panelBottomBottom;
	JSplitPane  splitPaneV,splitPaneVBottom;
	JTextArea ResultData;
	JComboBox<String> InputCombo,TagCombo;
	JLabel lbParsePage,lbSearchFile,lbSearch,lbTagSearch;
	JTextField searchExpression;
	JButton btnParse,btnSearch;
	Parse act;
	Search srch;
	
	/**
	 * Tvorba celeho GUI volaná z mainu
	 */
	public void createGUI()  {
		mainPanel = new JPanel(new BorderLayout());
		this.add(mainPanel);
			
		InputCombo = CreateCombo("input");
		String[] TagList = new String[]{"title","aka","akaTextTitle","akaTextAka"};
		TagCombo = new JComboBox<String>(TagList);
		
		// BOTTOM BOTOM PANEL - PANEL ZOBRAZENIA VYSLEDKU	
		
		panelBottomBottom= new JPanel();
		
		panelBottomBottom.setLayout( new FlowLayout() );
		JLabel lbParseredData = new JLabel("Vyparsované dáta");
		ResultData = new JTextArea();
		JScrollPane sp = new JScrollPane(ResultData); 
		sp.setPreferredSize(new Dimension(950, 875));
		panelBottomBottom.add(lbParseredData);
		panelBottomBottom.add(sp);
		
		
		lbSearchFile = new JLabel("      Zadajte vyraz ktorý chcete hladat: ");
		lbTagSearch =  new JLabel("Zadajte znacku ktoru chcete hladat: ");
		searchExpression = new JTextField();
		searchExpression.setPreferredSize(new Dimension(250, 20)); 
		//searchExpression.setText("Aarhus");
		btnSearch = new JButton("Search !");
		srch = new Search();
		srch.set(searchExpression,ResultData,TagCombo);
		btnSearch.addActionListener(srch);
		
		// BOTTOM TOP PANEL - SEARCH PANEL 
		panelBottomTop= new JPanel();
		panelBottomTop = new JPanel();
		panelBottomTop.setLayout( new FlowLayout() );
		panelBottomTop.add(lbTagSearch);
		panelBottomTop.add(TagCombo);
		panelBottomTop.add(lbSearchFile);
		panelBottomTop.add(searchExpression);
		panelBottomTop.add(btnSearch);
		
		splitPaneVBottom = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
		splitPaneVBottom.setLeftComponent(panelBottomTop);
		splitPaneVBottom.setRightComponent(panelBottomBottom);
		
		
		//TOP PANEL !!!! - PARSER PANEL
		
		panelTop = new JPanel();
		panelTop.setLayout( new FlowLayout() );
		lbParsePage = new JLabel("Zadajte požadovaný súbor na prehladávanie: ");
		InputCombo.setPreferredSize(new Dimension(500, 30));
		btnParse = new JButton("Parse !");
		act = new Parse();
		act.set(InputCombo,ResultData);
		btnParse.addActionListener(act);	
		panelTop.add(lbParsePage);
		panelTop.add(InputCombo);
		panelTop.add(btnParse);
		
		splitPaneV = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
		mainPanel.add( splitPaneV, BorderLayout.CENTER );
		splitPaneV.setLeftComponent( panelTop );
		splitPaneV.setRightComponent( splitPaneVBottom );
		
	}
	
	/**
	 * Dynamicke vytvorenie Comboboxu pre InputCombo Box
	 * @param FileName - Podadresar projektu s ktoreho sa vytvorí komac.
	 * @return - Vytvorený kombobox s pozadovanmi hodnotami
	 */
	private JComboBox<String> CreateCombo(String FileName) {
		String workingDir = System.getProperty("user.dir");
		File folder = new File(workingDir+"/"+FileName);
		File[] listOfFiles = folder.listFiles();
		List<String> nameArrayList = new ArrayList<String>();
		if (listOfFiles!=null)
		for(File file: listOfFiles){
			if (file.isFile()) {
			nameArrayList.add(file.getName());
			}
		}
		String[] ImgList = new String[nameArrayList.size()];
		for(int i = 0; i<nameArrayList.size();i++){
			ImgList[i] = nameArrayList.get(i);
		}	
		return new JComboBox<String>(ImgList);
	}

}
