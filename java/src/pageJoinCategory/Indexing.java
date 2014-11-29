

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

    final static Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

    Indexing(String fileName) {
        this.fileName = fileName;
    }

    public void run() throws Exception {
        CSVReader reader = new CSVReader(new FileReader(fileName));
        String[] nextLine;

        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = new DeleteByQueryRequestBuilder(client);
        deleteByQueryRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        deleteByQueryRequestBuilder.execute().actionGet();

        while ((nextLine = reader.readNext()) != null) {
            String[] arr = new String[nextLine.length - 1];

            for (int i = 1; i < nextLine.length; i++) {
                arr[i - 1] = nextLine[i];
            }

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
