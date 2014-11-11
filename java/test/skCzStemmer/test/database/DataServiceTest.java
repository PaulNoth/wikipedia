package skCzStemmer.test.database;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.testng.annotations.Test;

import skCzStemmer.services.data.DataDefaultService;
import skCzStemmer.services.data.WordTreeItem;
import skCzStemmer.utils.MyFilePaths;

public class DataServiceTest {

    private File sampleTreeFile = new File(MyFilePaths.SAMPLE_SK_DATA_XML.replace(".xml", ".anchor")); 
    private File fullTreeFile = new File(MyFilePaths.FULL_SK_DATA_XML.replace(".xml", ".anchor"));
    
    private DataDefaultService service1 = new DataDefaultService(new File(MyFilePaths.SAMPLE_TREE_FILE));
    private DataDefaultService service2 = new DataDefaultService(new File(MyFilePaths.FULL_TREE_FILE));
    
    @Test
    public void createTreeFromAnchorFile() throws JAXBException, XMLStreamException, IOException {

     WordTreeItem sample_root = service1.createTreeFromAnchorFile(sampleTreeFile);
     
     WordTreeItem full_root = service2.createTreeFromAnchorFile(fullTreeFile);
     
     sample_root.getChildren().size();
     
     full_root.getChildren().size();
    }
}
