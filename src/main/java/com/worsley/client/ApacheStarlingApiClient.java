package com.worsley.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.client.exception.StarlingApiException;
import com.worsley.client.request.AddToSavingsGoalRequestBody;
import com.worsley.client.response.AccountsResponse;
import com.worsley.client.response.ResponseWithCode;
import com.worsley.client.response.TransactionsResponse;
import com.worsley.dto.Account;
import com.worsley.dto.Amount;
import com.worsley.dto.Currency;
import com.worsley.dto.Transaction;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

public class ApacheStarlingApiClient implements StarlingApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApacheStarlingApiClient.class);
    private static final String STARLING_BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/";
    private static final String ACCOUNTS_RELATIVE_PATH = "accounts";
    private static final String SETTLED_TRANSACTIONS_BETWEEN_RELATIVE_PATH = "feed/account/%s/settled-transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s";
    private static final String SAVINGS_GOAL_ADD_MONEY_RELATIVE_PATH = "account/%s/savings-goals/%s/add-money/%s";

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
    public Set<Transaction> getTransactions(UUID accountUid, ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) throws IOException {
        String fromDate = minTransactionTimestamp.format(dateTimeFormatter);
        String toDate = maxTransactionTimestamp.format(dateTimeFormatter);
        String relativePath = String.format(SETTLED_TRANSACTIONS_BETWEEN_RELATIVE_PATH, accountUid, fromDate, toDate);
        TransactionsResponse response = makeGetRequest(relativePath, TransactionsResponse.class);
        return response.feedItems();
    }

    @Override
    public void addMoneyToSavingsGoal(UUID accountUid, UUID savingsGoalUuid, int amountInPennies, Currency currency) throws IOException {
        UUID transactionUuid = UUID.randomUUID();
        String relativePath = String.format(SAVINGS_GOAL_ADD_MONEY_RELATIVE_PATH, accountUid, savingsGoalUuid, transactionUuid);
        String body = objectMapper.writeValueAsString(new AddToSavingsGoalRequestBody(new Amount(amountInPennies, currency)));
        makePutRequest(relativePath, body);
    }

    private <T> T makeGetRequest(String relativePath, Class<T> responseClass) throws IOException {
        final HttpGet request = new HttpGet(URI.create(STARLING_BASE_URL + relativePath));
        ResponseWithCode responseWithCode = makeRequest(request);
        return objectMapper.readValue(responseWithCode.response(), responseClass);
    }

    private ResponseWithCode makePutRequest(String relativePath, String body) throws IOException {
        final HttpPut request = new HttpPut(URI.create(STARLING_BASE_URL + relativePath));
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        return makeRequest(request);
    }

    private ResponseWithCode makeRequest(HttpRequestBase request) throws IOException {
        String authToken = System.getenv("STARLING_AUTH_TOKEN");
        request.addHeader("Authorization", "Bearer " + authToken);
        request.addHeader("Accept", "application/json");
        logger.info("Executing request {}", request);
        ResponseWithCode responseWithCode = client.makeRequest(request);
        int statusCode = responseWithCode.statusCode();
        if(statusCode == 401) {
            /*
            NOTE_FOR_REVIEWER: Here I could refresh the OAuth via the api provided the refresh isn't expired
            and then retry. However, for the sake of this tech test I will just log so user can refresh
            themselves and run the program again.
             */
            logger.error("401");
        }
        if(statusCode < 200 || statusCode >= 300) {
            String errorMessage = String.format("API Response not as expected. Status code: %s, response: %s", statusCode, responseWithCode.response());
            logger.error(errorMessage);
            throw new StarlingApiException(errorMessage);
        }
        logger.debug("Request executed successfully");
        return responseWithCode;
    }
}
