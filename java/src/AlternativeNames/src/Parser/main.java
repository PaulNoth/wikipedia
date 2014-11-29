package Parser;

import java.io.IOException;

import javax.swing.JFrame;

/**
 * Spuscacia trieda
 * @author Samuel Benkovic
 *
 */
public class main {
	private static GUI gu;
	public static void main(String[] args) throws IOException {
		gu = new GUI();
		gu.createGUI();
		gu.setVisible(true);
		gu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gu.setSize(1000,1000);
	}
	public GUI getGu() {
		return gu;
	}
	public void setGu(GUI gu) {
		main.gu = gu;
	}
	
}