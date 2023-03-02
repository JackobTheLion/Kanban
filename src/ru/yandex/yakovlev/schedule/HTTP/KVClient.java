package ru.yandex.yakovlev.schedule.HTTP;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {

    String KVServerUrl;
    String API_TOKEN = "";
    HttpClient client = HttpClient.newHttpClient();

    public KVClient(String KVServerUrl) throws URISyntaxException, IOException, InterruptedException {
        this.KVServerUrl = KVServerUrl;
        registerAtKVServer();
    }

    public void registerAtKVServer() throws URISyntaxException, IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI(KVServerUrl + "/register"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        API_TOKEN = response.body();
    }

    public void saveAtKVServer(String key, String body) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(new URI(KVServerUrl + "/save/" + key + "?API_TOKEN=" + API_TOKEN))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.getMessage();
            e.getStackTrace();
        }
    }

    public String loadFromServer(String key) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(KVServerUrl + "/load/" + key + "?API_TOKEN=" + API_TOKEN))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return null;
        }
    }

    public void setAPI_TOKEN_DEBUG() {
        this.API_TOKEN = "DEBUG";
    }
}
