package sk.fiit.nemecek.jolana.server.services.database.service;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import sk.fiit.nemecek.jolana.server.services.database.data.KorpusItem;

@Test
@ContextConfiguration(locations = {"classpath:applicationContext.xml" })
public class DatabaseServiceTest extends AbstractTestNGSpringContextTests{

    @Autowired
    private DatabaseService service;
    
    @Test
    public void findSubTreeByLetter() {
        try {
            String pismeno = "x";
            KorpusItem item = service.findSubTreeByLetter(pismeno);
            
            Assert.assertTrue(item.getPismeno().equals(pismeno));
            Assert.assertNotNull(item.getChildren().getList().get(0).getChildren().getList().get(0).getId());
        } catch (JAXBException | XMLStreamException | IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
    @Test
    public void extractMediaWikiTextForLearning(){
        try {
            service.extractMediaWikiTextForLearning();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
