package DisambiguationParse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame; import javax.swing.JPanel; import javax.swing.JComboBox; import javax.swing.JButton; import javax.swing.JLabel; import javax.swing.JList; import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout; import java.awt.event.ActionListener; import java.awt.event.ActionEvent;

public class Disambiguation_FinalService  {
	
	
	

	static Map<String, String> hashMapFinal = new HashMap<String,  String>(); 
	public void openservice(){
		
		
		
		
		System.out.println("Please wait, creating memory index ...");
		File file = new File("sparsovane.txt");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		String title="";
		String link ="";
		String anchors = "";
		String desc = "";
		try {
			while ((line = br.readLine()) != null) {
			//System.out.println(line);
				if(line.indexOf("<title>")>=0){
					
					title = line.substring(9,line.length()-8);
					//System.out.println(title);
	
				}
				
				if(line.indexOf("<link>")>=0){					
				link	= line.substring(8,line.length()-7);
				}
					
				if(line.indexOf("<anchors>")>=0){
				anchors	= line.substring(11,line.length()-10);	
				}
				
				if(line.indexOf("<desc>")>=0 && line.indexOf("<desc></desc>")<0){
					desc	= line.substring(12,line.length()-7);	
					
				
				if(hashMapFinal.containsKey(title)){
					
					String actual = hashMapFinal.get(title);
					actual = actual + System.getProperty("line.separator") + "LINK: "+link + System.getProperty("line.separator") + "ANCHORS: "+anchors + System.getProperty("line.separator") + "DESC: "+desc+ System.getProperty("line.separator")+ System.getProperty("line.separator");
					hashMapFinal.put(title, actual);
				}else
				{
					String actual ="";
					actual = actual + System.getProperty("line.separator") + "LINK: "+link + System.getProperty("line.separator") + "ANCHORS: "+anchors + System.getProperty("line.separator") + "DESC: "+desc+ System.getProperty("line.separator")+ System.getProperty("line.separator");
					hashMapFinal.put(title, actual);
					
					
				}
					
					//we have four, we store it into hashmap 
					
					
					
				}
				
				
			}
			
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		guiFrame.setTitle("Service GUI"); 
		guiFrame.setSize(530,600);  //This will center the JFrame in the middle of the screen guiFrame.setLocationRelativeTo(null);
		final JPanel comboPanel = new JPanel();
		comboPanel.setLayout(null);
		final JTextArea textArea = new JTextArea();
		final JTextArea textArea2 = new JTextArea();
		JScrollPane scrollPane1 = new JScrollPane(textArea2);
		JScrollPane scrollPane = new JScrollPane(textArea); 
		JButton a = new JButton("Nájdi");
		
		  a.addActionListener(new ActionListener() {
			  
	            public void actionPerformed(ActionEvent e)
	            {
	                //Execute when button is pressed
	                String text = textArea.getText();
	                String write = hashMapFinal.get(text);
	                textArea2.setText(write);
	                
	            }
	        });  
		
		scrollPane.setBounds(0, 0, 500, 50);
		a.setBounds(0, 50, 100, 20);
		scrollPane1.setBounds(0, 100, 500, 450);
		
		textArea2.setLineWrap(true);
		comboPanel.add(scrollPane);
		comboPanel.add(scrollPane1);
		comboPanel.add(a);
		
		
		guiFrame.add(comboPanel);
		guiFrame.setVisible(true);
		/*System.out.println("Please insert Name of Disambiguation page");
		BufferedReader br2 = new BufferedReader(new
                InputStreamReader(System.in));
		String userInput = "";
		 do {
	         
	          try {
				userInput = (String) br2.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          System.out.println("You entered : " + userInput);
	          
	          
	          String write = hashMapFinal.get(userInput);
	          System.out.println(write);
	          
	          
	        } while(!userInput.equals("quit"));*/
		
		
		
		
	}
	
	
	
}
