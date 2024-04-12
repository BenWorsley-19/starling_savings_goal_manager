package com.worsley.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.client.response.AccountsResponse;
import com.worsley.client.response.StarlingResponse;
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

    private final StarlingApiClient starlingApiClient;
    private final ObjectMapper objectMapper;

    public AccountsClient(StarlingApiClient starlingApiClient) {
        this.starlingApiClient = starlingApiClient;
        this.objectMapper = new ObjectMapper(); // TODO?
    }

    public Set<Account> getAccounts() throws URISyntaxException, IOException, InterruptedException { // TODO throe all these? maybe retry
        /*
        NOTE_FOR_REVIEWER: decided to throw URISyntaxException because if there is a     problem with the uri
        that's a programming error, and we can't recover from that, so we should error.
         */
//        StarlingResponse response = starlingApiClient.makeGetRequest("accounts");
        AccountsResponse response = starlingApiClient.makeGetRequest("accounts", AccountsResponse.class);

//        AccountsResponse accountsResponse = objectMapper.readValue(response.response(), new TypeReference<AccountsResponse>(){});
//        Set<Account> accounts = accountsResponse.accounts();

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

        return response.accounts();
    }
}
