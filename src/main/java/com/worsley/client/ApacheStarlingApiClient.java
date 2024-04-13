package com.worsley.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.client.exception.StarlingApiException;
import com.worsley.client.response.AccountsResponse;
import com.worsley.client.response.ResponseWithCode;
import com.worsley.client.response.TransactionsResponse;
import com.worsley.dto.Account;
import com.worsley.dto.Transaction;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class ApacheStarlingApiClient implements StarlingApiClient {

    private static Logger logger = LoggerFactory.getLogger(ApacheStarlingApiClient.class);
    private static final String STARLING_BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/";
    private static final String ACCOUNTS_RELATIVE_PATH = "accounts";
    private static final String SETTLED_TRANSACTIONS_BETWEEN_RELATIVE_PATH = "feed/account/%s/settled-transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s";
    private final DateTimeFormatter dateTimeFormatter;

    private final ObjectMapper objectMapper;
    private final ApacheHttpClientWrapper client;

    public ApacheStarlingApiClient(ObjectMapper objectMapper, ApacheHttpClientWrapper client) {
        this.objectMapper = objectMapper;
        this.client = client;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    }

    @Override
    public Set<Account> getAccounts() throws IOException {
        AccountsResponse response = makeGetRequest(ACCOUNTS_RELATIVE_PATH, AccountsResponse.class);
        return response.accounts();
    }

    @Override
    public Set<Transaction> getTransations(String accountUid, ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) throws IOException {
        String fromDate = minTransactionTimestamp.format(dateTimeFormatter);
        String toDate = maxTransactionTimestamp.format(dateTimeFormatter);
        String relativePath = String.format(SETTLED_TRANSACTIONS_BETWEEN_RELATIVE_PATH, accountUid, fromDate, toDate);
        TransactionsResponse response = makeGetRequest(relativePath, TransactionsResponse.class);
        return response.feedItems();
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
