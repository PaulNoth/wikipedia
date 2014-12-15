package skCzStemmer.main;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import skCzStemmer.services.data.DataDefaultService;
import skCzStemmer.services.extractor.MediaWikiExtractor;
import skCzStemmer.services.stemmer.StemmerDefaultService;
import skCzStemmer.utils.MyFilePaths;

public class Main {

    public static void main(String[] args) {

        int jop = JOptionPane.showConfirmDialog(null, "Process MediaWiki dump?", "SKCZStemmer", JOptionPane.YES_NO_OPTION);

        if (jop == JOptionPane.YES_OPTION) {
            JFileChooser chooser = new JFileChooser(new File("C:/"));
            int returnVal = chooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                // GET ANCHOR FILE
                File fullSKDump = chooser.getSelectedFile();
                MediaWikiExtractor extractorService = new MediaWikiExtractor(1000, fullSKDump);
                try {
                    extractorService.extractMediaWikiAnchorsFromAnchorFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // GET TREE FILE
                File treeFile = new File(fullSKDump.getParent() + File.separator + MyFilePaths.TREE_FILE_NAME);
                DataDefaultService databaseService = new DataDefaultService(treeFile);
                try {
                    databaseService.createTreeFromAnchorFile(new File(fullSKDump.getParent() + File.separator + fullSKDump.getName().replace(".xml", ".anchor")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // STEMM TEXT
                StemmerDefaultService stemmerService = new StemmerDefaultService(100, treeFile);
                JOptionPane.showMessageDialog(null, "Choose test file", "SKCZStemmer", JOptionPane.DEFAULT_OPTION );
                JFileChooser testChooser = new JFileChooser(new File("C:/"));
                int returnVal2 = testChooser.showOpenDialog(null);
                if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                    stemmerService.stemmAnchors(testChooser.getSelectedFile());
                }
            }
            System.exit(0);
        }
    }
}
