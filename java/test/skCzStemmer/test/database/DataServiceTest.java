package skCzStemmer.test.database;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.testng.annotations.Test;

import skCzStemmer.services.data.DataDefaultService;

public class DataServiceTest {

    private DataDefaultService service = new DataDefaultService();

    @Test
    public void createTreeFromAnchorFile() throws JAXBException, XMLStreamException, IOException {
//        WordTreeItem root = service.createTreeFromAnchorFile(new File(MyFilePaths.PAGESLOCATION + File.separator + "Židia etnikum.output"));
//        Assert.assertTrue(root.getChildren().size() > 0);
    }
}
