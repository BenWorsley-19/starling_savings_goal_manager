package com.worsley.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TransactionsClient {

    private final HttpClient httpClient;

    public TransactionsClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    public void getTransactionsInWeek(String accountId, String week) throws URISyntaxException {

//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI("TODO"))
//                .GET()
////            .header(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
//                .timeout(Duration.ofSeconds(30))
//                .build();
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        int statusCode = response.statusCode();
    }
}
