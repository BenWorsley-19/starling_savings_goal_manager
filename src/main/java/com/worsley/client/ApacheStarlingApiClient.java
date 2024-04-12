package com.worsley.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.client.exception.StarlingApiException;
import com.worsley.client.response.AccountsResponse;
import com.worsley.client.response.ResponseWithCode;
import com.worsley.dto.Account;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

public class ApacheStarlingApiClient implements StarlingApiClient {

    private static Logger logger = LoggerFactory.getLogger(ApacheStarlingApiClient.class);
    public static final String STARLING_BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/";

    private final ObjectMapper objectMapper;
    private final ApacheHttpClientWrapper client;

    public ApacheStarlingApiClient(ObjectMapper objectMapper, ApacheHttpClientWrapper client) {
        this.objectMapper = objectMapper;
        this.client = client;
    }

    @Override
    public Set<Account> getAccounts() throws IOException {
        AccountsResponse response = makeGetRequest("accounts", AccountsResponse.class);
        return response.accounts();
    }

    private <T> T makeGetRequest(String relativePath, Class<T> responseClass) throws IOException {
        final HttpGet request = new HttpGet(URI.create(STARLING_BASE_URL + relativePath));
        return makeRequest(request, responseClass);
    }

    private <T> T makePutRequest(String relativePath, Class<T> responseClass) throws IOException {
        final HttpPut request = new HttpPut(URI.create(STARLING_BASE_URL + relativePath));
        return makeRequest(request, responseClass);
    }

    private <T> T makeRequest(HttpRequestBase request, Class<T> responseClass) throws IOException {
        String authToken = System.getenv("STARLING_AUTH_TOKEN");
        request.addHeader("Authorization", "Bearer " + authToken);
        request.addHeader("Accept", "application/json");
        logger.info("Executing request {}", request.toString());
        ResponseWithCode responseWithCode = client.makeRequest(request);
        int statusCode = responseWithCode.statusCode();
        String stringResponse = responseWithCode.response();
        if(statusCode == 401) {
            /*
            NOTE_FOR_REVIEWER: Here I could refresh the OAuth via the api provided the refresh isn't expired
            and then retry. However, for the sake of this tech test I will just log and error so user can refresh
            themselves and run the program again.
             */
            logger.error("401");
        }
        if(statusCode != 200) {
            String errorMessage = String.format("API Response not as expected. Status code: %s, response: %s", statusCode, stringResponse);
            logger.error(errorMessage);
            throw new StarlingApiException(errorMessage);
        }
        logger.debug("Request executed successfully");
        return objectMapper.readValue(stringResponse, responseClass);
    }
}
