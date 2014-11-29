import java.util.ArrayList;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

/**
 *
 * @author patrik
 */
public class Search {

    final static int pageSize = 40;

    final static Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

    void search(String phrase) {
        SearchResponse search = client.prepareSearch().setQuery(
                QueryBuilders.boolQuery()
                .should(QueryBuilders.wildcardQuery("page", "*" + phrase + "*"))
                .should(QueryBuilders.wildcardQuery("category", "*" + phrase + "*"))
                .minimumNumberShouldMatch(1)
        ).setFrom(0).setSize(50).execute().actionGet();

        System.out.println("PAGE" + repeatSpace(pageSize - 4) + "CATEGORIES");

        for (SearchHit hit : search.getHits()) {
            String page = (String) hit.getSource().get("page");
            ArrayList categories = (ArrayList) hit.getSource().get("category");

            System.out.print(page + repeatSpace(pageSize - page.length()));
            for (Object category : categories) {
                System.out.print("'" + category + "', ");
            }
            System.out.println();
        }
    }

    String repeatSpace(int num) {
        if (num < 0) {
            return "";
        }
        StringBuilder s = new StringBuilder(num);
        for (int i = 0; i < num; i++) {
            s.append(" ");
        }

        return s.toString();
    }

    void close() {
        client.close();
    }
}
