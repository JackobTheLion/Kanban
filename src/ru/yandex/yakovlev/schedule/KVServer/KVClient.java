package ru.yandex.yakovlev.schedule.KVServer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class KVClient {

    URI uri;
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

    HttpRequest request = requestBuilder
            .GET()    // указываем HTTP-метод запроса
            .uri(uri) // указываем адрес ресурса
            .version(HttpClient.Version.HTTP_1_1) // указываем версию протокола HTTP
            .header("Accept", "text/html") // указываем заголовок Accept
            .build(); // заканчиваем настройку и создаём ("строим") HTTP-запрос

    public KVClient(String path) {
        uri = URI.create(path);
    }


}
