package br.com.spedison.main;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class Teste {

    public static void main(String[] args) throws IOException {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        HttpEntity httpEntity = restClient.performRequest(new Request("GET", "teste/_count?pretty=true")).getEntity();
        String txt = new String(httpEntity.getContent().readAllBytes());

        System.out.println(txt);

        System.exit(0);
/*
// Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

// And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);
*/

    }

}
