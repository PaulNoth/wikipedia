

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author patrik
 */
public class Indexing {

    String fileName;

    // create elasticsearch connection
    final static Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

    Indexing(String fileName) {
        this.fileName = fileName;
    }

    /*
        indexing data to elasticsearch from csv file
    */
    public void run() throws Exception {
        // remove all data from elasticsearch
        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = new DeleteByQueryRequestBuilder(client);
        deleteByQueryRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        deleteByQueryRequestBuilder.execute().actionGet();

        CSVReader reader = new CSVReader(new FileReader(fileName));
        String[] nextLine;
        
        while ((nextLine = reader.readNext()) != null) {
            String[] arr = new String[nextLine.length - 1];

            for (int i = 1; i < nextLine.length; i++) {
                arr[i - 1] = nextLine[i];
            }

            // insert page with categories into elasticsearch
            IndexResponse response = client.prepareIndex("wikipedia", "joins")
                    .setSource(
                            jsonBuilder().startObject()
                            .field("page", nextLine[0])
                            .array("category", arr)
                            .endObject()
                    )
                    .execute()
                    .actionGet();
        }
        client.close();
    }
}
