package gui;

import indexing.IndexGeneratedArtcileXmlFile;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

import ttltoxml.TtlToXml;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

/**
 * @author Skrisa  Július
 *
 */
public class MainFrameDBpedia32 extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private IndexGeneratedArtcileXmlFile ind;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrameDBpedia32 frame = new MainFrameDBpedia32();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrameDBpedia32() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 573);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(25, 13, 567, 22);
		contentPane.add(textField);
		textField.setColumns(10);
		
		final DefaultListModel listA = new DefaultListModel();
		final List<Integer> listDoc = new ArrayList<Integer>();
		
		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(25, 81, 855, 432);
		JScrollPane scrollBar = new JScrollPane(textArea);
		scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollBar.setBounds(258, 81, 622, 432);
		contentPane.add(scrollBar);
		
		JLabel lblResults = new JLabel("Results:");
		lblResults.setBounds(25, 52, 56, 16);
		contentPane.add(lblResults);
		
		JButton btnGenerateXml = new JButton("Generate XML");
		btnGenerateXml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				TtlToXml.GenerateXml(); // generating xml file - parsing ttls + saving on disk
				textArea.append("File Generated!");
			}
		});
		btnGenerateXml.setBounds(750, 12, 130, 25);
		contentPane.add(btnGenerateXml);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				listDoc.clear();
				listA.clear();
				ScoreDoc[] results = ind.search(textField.getText()); // searching in index
				Document doc = null;
				for(int i = 0; i<results.length; i++){ // adding data to gui for each record
					try {
						doc = ind.searcher.doc(results[i].doc);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(doc != null){
						
						listA.addElement(doc.get("Lbl"));
						listDoc.add(results[i].doc);
					}
				}
				if(results.length == 0){
					textArea.append("No matching result");
				}
			}

			
		});
		btnSearch.setBounds(604, 12, 97, 25);
		contentPane.add(btnSearch);
		
		final JButton btnNewButton = new JButton("Index loaded file");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ind = new IndexGeneratedArtcileXmlFile(); // createing index for genereted xml file
				ind.indexLoadedXml();
				try {
					ind.w.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				btnNewButton.disable();
			}
		});
		btnNewButton.setBounds(750, 43, 130, 25);
		contentPane.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 81, 221, 432);
		contentPane.add(scrollPane);
		
		final JList list = new JList(listA);
		scrollPane.setViewportView(list);
		
		list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent evt) {
            	Document doc;
            	try {
					doc = ind.searcher.doc(listDoc.get(list.getSelectedIndex()));
					showResults(doc);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            
            private void showResults(Document doc) {
            	textArea.setText("");
				textArea.append(doc.get("Lbl") + ":\n");
				textArea.append("Wikilink: " + doc.get("Wklnk") + "\n");
				textArea.append("Abstract: " + doc.get("Abstrct") + "\n");
				
				int i = 0;
				String s ="Rfrnc"; 
				textArea.append("References: \n");
				while(doc.get(s + i) != null){
					textArea.append("\t" + doc.get(s + i) + "\n");
					i++;
				}
				
				i = 0;
				s ="Rdrct"; 
				textArea.append("Redirects: \n");
				while(doc.get(s + i) != null){
					textArea.append("\t" + doc.get(s + i) + "\n");
					i++;
				}
				
				i = 0;
				s ="Extrnl"; 
				textArea.append("External links: \n");
				while(doc.get(s + i) != null){
					textArea.append("\t" + doc.get(s + i) + "\n");
					i++;
				}
			
				i = 0;
				s ="Ctgr"; 
				textArea.append("Caregories: \n");
				while(doc.get(s + i) != null){
					textArea.append("\t" + doc.get(s + i) + "\n");
					i++;
				}
				textArea.append("\n");
			}

        });
	}
}
