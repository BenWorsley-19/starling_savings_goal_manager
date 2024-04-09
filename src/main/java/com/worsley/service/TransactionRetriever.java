package com.worsley.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TransactionRetriever {

    public TransactionRetriever(HttpClient httpClient) {

    }


    public void getTransactionsInWeek(String accountId, String week) throws URISyntaxException {
        /*
        NOTE_FOR_REVIEWER: decided to throw URISyntaxException because if there is a problem with the uri
        that's a programming error, and we can't recover from that, so we should error.
         */
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("TODO"))
                .GET()
//            .header(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
                .timeout(Duration.ofSeconds(30))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
    }
}
