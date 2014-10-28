package skCzStemmer.test.database;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.testng.Assert;
import org.testng.annotations.Test;

import skCzStemmer.services.database.data.KorpusItem;
import skCzStemmer.services.database.data.KorpusItemList;

public class DatabaseTest {

    @Test
    public static void createSuffixTreeTest() throws JAXBException {

        KorpusItem item = new KorpusItem();
        item.setPismeno("a");
        KorpusItem item2 = new KorpusItem();
        item2.setPismeno("x");
        KorpusItem item3 = new KorpusItem();
        item3.setPismeno("c");
        
        item.addKorpusItem(item2);
        item2.addKorpusItem(item3);
        
        KorpusItem item4 = new KorpusItem();
        item4.setPismeno("x");
        KorpusItem item5 = new KorpusItem();
        item5.setPismeno("y");
        KorpusItem item6 = new KorpusItem();
        item6.setPismeno("z");
        
        item4.addKorpusItem(item5);
        item5.addKorpusItem(item6);
        
        KorpusItemList root = new KorpusItemList();
        root.addItem(item);
        root.addItem(item4);
        
        JAXBContext jaxbContext = JAXBContext.newInstance(KorpusItem.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
     
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
         
        //Marshal the employees list in console
        jaxbMarshaller.marshal(root, System.out);
         
        //Marshal the employees list in file
        File f = new File("letters.xml");
        jaxbMarshaller.marshal(root, f);
        
        Assert.assertTrue(f.length() > 0);
    }
}
