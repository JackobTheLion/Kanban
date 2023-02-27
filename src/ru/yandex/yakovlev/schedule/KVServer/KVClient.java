package ru.yandex.yakovlev.schedule.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {

    String KVServerUrl = "http://" + KVServer.hostname + ":" + KVServer.PORT;
    String API_TOKEN = "";
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public KVClient() throws URISyntaxException, IOException, InterruptedException {
        registerAtKVServer();
    }

    public void registerAtKVServer() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = requestBuilder
                .GET()
                .uri(new URI(KVServerUrl + "/register"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpResponse<String> response = client.send(request, handler);

        API_TOKEN = response.body();
    }

    public void saveAtKVServer(String key, String body) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(new URI(KVServerUrl + "/save/" + key + "?API_TOKEN=" + API_TOKEN))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public String loadFromServer(String key) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = requestBuilder
                .GET()
                .uri(new URI(KVServerUrl + "/load/" + key + "?API_TOKEN=" + API_TOKEN))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
