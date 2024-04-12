package com.worsley.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.client.response.AccountsResponse;
import com.worsley.dto.Account;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class AccountsClient {

    public static final String STARLING_BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/"; // TODO find home for this constant

//    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AccountsClient() { //(HttpClient httpClient) {
//        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper(); // TODO?
    }

    public Set<Account> getAccounts() throws URISyntaxException, IOException, InterruptedException { // TODO throe all these? maybe retry

        Set<Account> accounts = new HashSet<>();

        // TODO update readme with this
        /*
        NOTE_FOR_REVIEWER: So not to store the secret in plain text.
         */
        String authToken = System.getenv("STARLING_AUTH_TOKEN");

        final HttpGet request = new HttpGet(URI.create(STARLING_BASE_URL + "accounts"));
        request.addHeader("Authorization", "Bearer " + authToken);
        request.addHeader("Accept", "application/json");

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(request)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                // TODO
            }
            AccountsResponse accountsResponse = objectMapper.readValue(EntityUtils.toString(response.getEntity()), new TypeReference<AccountsResponse>(){});
            accounts = accountsResponse.accounts();
        }

        // TODO explain reason for issue
//        HttpRequest request = HttpRequest.newBuilder()
//            .GET()
//            .uri(new URI(STARLING_BASE_URL + "accounts"))
//            .header("Authorization", "Bearer " + authToken)
//            .header("Accept", "*/*")
////            .header("User-Agent", "BenWorsley")
////            .header("Accept-Encoding", "gzip, deflate, br")
////            .header("Host", "www.bworsley.com")
////                .header("User-Agent", "BenWorsley")
//            .timeout(Duration.ofSeconds(30))
//            .build();
//        HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return accounts;
    }
}
