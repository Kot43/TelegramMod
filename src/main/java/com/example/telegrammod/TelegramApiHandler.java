package com.example.telegrammod;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TelegramApiHandler {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void sendMessage(String text) {
        new Thread(() -> {
            try {
                String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                        TelegramModClient.telegramToken,
                        TelegramModClient.ownerId,
                        URLEncoder.encode(text, StandardCharsets.UTF_8));

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
