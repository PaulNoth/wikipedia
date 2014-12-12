package Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit Test na hladanie vyrazov vo vyparsovanom subore.
 * @author Samuel Benkovic
 *
 */
public class TestParser {
	main mainclass = new main();
	JButton btnParse;
	JComboBox<String> InputCombo;
	@Before 
	public void init(){
		mainclass.setGu(new GUI());
		mainclass.getGu().createGUI();
		btnParse = mainclass.getGu().btnParse;
		InputCombo = mainclass.getGu().InputCombo;
	}
	/**
	 * Test Parsovania súboru, ci bol vytvorený a prepísaný.
	 * jedná sa dumb enwiki-latest-pages-articles1.xml-p000000010p000010000
	 */
	@Test
	public void TestNoneresult() throws IOException {
		InputCombo.setSelectedItem("enwiki-latest-pages-articles1.xml-p000000010p000010000");
		btnParse.doClick();
		String parseredFilepath = System.getProperty("user.dir")+"/parsered/"+ InputCombo.getSelectedItem().toString();
		Path p = Paths.get(parseredFilepath);
	    BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
	    long milliseconds = view.lastModifiedTime().to(TimeUnit.MILLISECONDS);
	    if((milliseconds > Long.MIN_VALUE) && (milliseconds < Long.MAX_VALUE))
        {
            Date creationDate =
                    new Date(view.lastModifiedTime().to(TimeUnit.MILLISECONDS));
            Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
            int currentDay = localCalendar.get(Calendar.DATE);
            int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
            int currentYear = localCalendar.get(Calendar.YEAR);

            //test dna mesiaca a roku
            Assert.assertTrue(currentDay == creationDate.getDate());
            Assert.assertTrue(currentMonth == creationDate.getMonth() + 1);
            Assert.assertTrue(currentYear == creationDate.getYear() + 1900);
        }
	    
	   
	}
	
}

