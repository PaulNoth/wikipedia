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

//    private File sampleTreeFile = new File(MyFilePaths.SAMPLE_SK_DATA_XML.replace(".xml", ".anchor")); 
//    private DataDefaultService service1 = new DataDefaultService(new File(MyFilePaths.SAMPLE_TREE_FILE));
    
//    private File fullSKTreeFile = new File(MyFilePaths.FULL_SK_DATA_XML.replace(".xml", ".anchor"));
//    private DataDefaultService service2 = new DataDefaultService(new File(MyFilePaths.FULL_SK_TREE_FILE));
    
    private File fullCZTreeFile = new File(MyFilePaths.FULL_CZ_DATA_XML.replace(".xml", ".anchor"));
    private DataDefaultService service3 = new DataDefaultService(new File(MyFilePaths.FULL_CZ_TREE_FILE));
    
    @Test
    public void createTreeFromAnchorFile() throws JAXBException, XMLStreamException, IOException {

//     WordTreeItem sample_root = service1.createTreeFromAnchorFile(sampleTreeFile);
//      sample_root.getChildren().size();
        
//     WordTreeItem fullSkRoot = service2.createTreeFromAnchorFile(fullSKTreeFile);
//     fullSkRoot.getChildren().size();
     
     WordTreeItem fullCzRoot = service3.createTreeFromAnchorFile(fullCZTreeFile);
     fullCzRoot.getChildren().size();
     
    }
}
