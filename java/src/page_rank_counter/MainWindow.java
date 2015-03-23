import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.JTextPane;
import java.awt.TextArea;
import java.awt.Button;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainWindow {

	private static JFrame frame;
	private JTextField txtInputFilesDirectory;
	private JSeparator separator;
	private JButton btnNewButton;
	private JTextField txtInsertPageName;
	private JButton btnNewButton_1;
	private WindowInterface wi;

	/**
	 * Launch the application.
	 */
	public void startWindow()
	{
		try {
			MainWindow window = new MainWindow();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();

		wi = new WindowInterface();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 198);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtInputFilesDirectory = new JTextField("Input files directory location");
		//txtInputFilesDirectory.setText("Input files directory location");
		txtInputFilesDirectory.setBounds(10, 11, 414, 22);
		frame.getContentPane().add(txtInputFilesDirectory);
		txtInputFilesDirectory.setColumns(10);
		
		final JButton btnParseFiles = new JButton("Parse files");
		btnParseFiles.setEnabled(false);
		btnParseFiles.setBounds(142, 44, 136, 23);
		frame.getContentPane().add(btnParseFiles);
		
		separator = new JSeparator();
		separator.setForeground(Color.GRAY);
		separator.setBounds(10, 78, 414, 2);
		frame.getContentPane().add(separator);
		
		btnNewButton = new JButton("Count pagerank");
		btnNewButton.setEnabled(false);
		btnNewButton.setBounds(288, 44, 136, 23);
		frame.getContentPane().add(btnNewButton);
		
		txtInsertPageName = new JTextField();
		txtInsertPageName.setText("Insert page name to see pagerank");
		txtInsertPageName.setBounds(10, 91, 414, 20);
		frame.getContentPane().add(txtInsertPageName);
		txtInsertPageName.setColumns(10);
		
		btnNewButton_1 = new JButton("Find page rank");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setBounds(288, 122, 136, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		final JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(10, 122, 268, 22);
		frame.getContentPane().add(textPane);
		
		JButton btnNewButton_2 = new JButton("Set directory");
		btnNewButton_2.setBounds(10, 44, 122, 23);
		frame.getContentPane().add(btnNewButton_2);
		//action listener for button set directory
		btnNewButton_2.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  wi.setPath(txtInputFilesDirectory.getText());
			  btnParseFiles.setEnabled(true);
			  btnNewButton_1.setEnabled(true);

		  }
		});
		//action listener for button parse files. This starts the file parser for input file sample pages.
		btnParseFiles.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  wi.parsePageFile();
			  btnNewButton.setEnabled(true);

		  }
		});
		//action listener for button count pageRank. This creates the links file and counts the page rank algorithm
		btnNewButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  wi.parseLinksfile();
			  wi.countPageRank();
			  btnNewButton_1.setEnabled(true);

		  }
		});
		
		//action listener for button fin page rank. This finds the pagerank in final file. If this file doesn't exist 
		//it creates an error message window.
		btnNewButton_1.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  String pageRank = wi.getPageRank(txtInsertPageName.getText());
			  if(pageRank != null)
			  {
				  textPane.setText(pageRank + "\n");  
			  }
			  else
			  {
				  JOptionPane.showMessageDialog(MainWindow.frame, "File not found. Please check if the input directory path is correct and/or create the file with options above.");
			  }

		  }
		});
	}
}
