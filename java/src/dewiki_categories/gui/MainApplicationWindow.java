package dewiki_categories.gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;

import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.IOException;

import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainApplicationWindow extends JDialog {

	private static final long serialVersionUID = -2569875329389936464L;

	private static JDialog maindialog;
	private static JDialog waitDialog;
	private final JComboBox myComboBox;
	private final JTextArea myTextArea;
	private final JPanel contentPanel = new JPanel();
	private static BackgroundData bgd = null;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MainApplicationWindow dialog = new MainApplicationWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			maindialog = dialog;
			
			//init BACKGROUND data
			bgd = new BackgroundData(waitDialog);
			
	        waitDialog.setModal(true);  
			waitDialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MainApplicationWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					bgd.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		final JDialog d = new JDialog();  
        JPanel p1 = new JPanel(new GridBagLayout());  
        p1.add(new JLabel("Prosim cakajte, pripravujem data..."), new GridBagConstraints());  
        d.getContentPane().add(p1);  
        d.setSize(300,100);  
        d.setLocationRelativeTo(this);  
        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        waitDialog = d;
				
		setTitle("Wiki kategorie");
		setBounds(100, 100, 614, 455);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{588, 0};
			gbl_panel.rowHeights = new int[]{23, 0};
			gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				JButton btnNewButton_1 = new JButton("Vycistit textove pole");
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						clearTextArea();
					}
				});
				GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
				gbc_btnNewButton_1.anchor = GridBagConstraints.NORTHEAST;
				gbc_btnNewButton_1.gridx = 0;
				gbc_btnNewButton_1.gridy = 0;
				panel.add(btnNewButton_1, gbc_btnNewButton_1);
			}
		}
		{
			JTextArea mainTextArea = new JTextArea();
			mainTextArea.setEditable(false);
			mainTextArea.setBackground(Color.LIGHT_GRAY);
			myTextArea = mainTextArea;
		}
		{
			JScrollPane scrollPane = new JScrollPane(myTextArea);
			contentPanel.add(scrollPane, BorderLayout.CENTER);
		}
		
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.SOUTH);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblNewLabel = new JLabel("Prehladaj na zaklade nazvu clanku alebo kategorie (pouzi cele slovo, nie iba cast)");
				panel.add(lblNewLabel, BorderLayout.NORTH);
			}
			{
				JPanel textPanel = new JPanel();
				panel.add(textPanel);
				textPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
				{
					textField = new JTextField();
					textPanel.add(textField);
					textField.setColumns(10);
				}
				{
					JButton enterButton = new JButton("Clanok alebo kategoria");
					enterButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							if(!textField.getText().isEmpty())
							{
								RefreshArtOrCateg( textField.getText() );
							}
						}
					});
					textPanel.add(enterButton);
					enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				}
			}
			{
				JPanel buttonPane = new JPanel();
				panel.add(buttonPane, BorderLayout.SOUTH);
				{
					JButton exitlButton = new JButton("Koniec");
					exitlButton.addActionListener(new ActionListener() {					
						@Override
						public void actionPerformed(ActionEvent arg0) {
							Exit();						
						}
					});
					{
						JButton btnNewButton = new JButton("Zobraz");
						btnNewButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {							
								Refresh(myComboBox.getSelectedIndex());
							}
						});
						buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
						{
							JLabel lblNewLabel_1 = new JLabel("Preddefinovane statistiky: ");
							buttonPane.add(lblNewLabel_1);
						}
						{
							JComboBox comboBox = new JComboBox();
							comboBox.setModel(new DefaultComboBoxModel(new String[] {"Porovnanie kategorii z XML a SQL", "Priemerny pocet kategorii", "Kategorie s max a min poctom pouziti (podla XML)", "Kategorie s max a min poctom pouziti (podla SQL)"}));
							myComboBox = comboBox;
							buttonPane.add(comboBox);
						}
						buttonPane.add(btnNewButton);
					}
					buttonPane.add(exitlButton);
				}
			}
		}
	}
	
	private void Refresh(int selectedIndex)
	{
		switch(selectedIndex)
		{
			case 0:
				updateTextArea(bgd.categoriesComparison());
				break;
			case 1:
				updateTextArea(bgd.averageCategories());
				break;
			case 2:
				updateTextArea(bgd.maxminCategoriesFromXML());
				break;
			case 3:
				updateTextArea(bgd.mostAndLeastUsedCategories());
				break;
			default:
					break;
		}
	}
	
	private void RefreshArtOrCateg(String str)
	{
		updateTextArea(bgd.getArticleOrCategory(str));
	}
	
	private void updateTextArea(List<String> lines)
	{	
		if(lines.isEmpty())
		{
			myTextArea.append("Ziadne vysledky :(\n");
		}
		
		for(String line : lines)
		{
			myTextArea.append(line + "\n");
		}
		
		myTextArea.append("------------------------------------------\n");
	}
	
	private void clearTextArea()
	{
		myTextArea.setText(null);
	}
	
	private void Exit()
	{
		//exit
		try {
			bgd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		maindialog.dispose();
	}

}
