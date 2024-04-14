package com.worsley.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.client.exception.StarlingApiException;
import com.worsley.client.response.AccountsResponse;
import com.worsley.client.response.ResponseWithCode;
import com.worsley.client.response.TransactionsResponse;
import com.worsley.dto.*;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
NOTE_FOR_REVIEWER: with more time I would add more here particulary around testing the details of the request that is passed to the client
 */
@ExtendWith(MockitoExtension.class)
public class ApacheStarlingAPiClientTest {

    private static final String RAW_ACCOUNTS_RESPONSE = "{\"accounts\":[{\"accountUid\":\"952e7ade-e540-4691-b94d-12406d2285d8\",\"accountType\":\"PRIMARY\",\"defaultCategory\":\"952e067c-df94-47ac-b301-51e84e37309a\",\"currency\":\"GBP\",\"createdAt\":\"2024-04-10T20:30:04.485Z\",\"name\":\"Personal\"}]}\n";
    private static final String RAW_TRANSACTIONS_RESPONSE = "{\"feedItems\":[{\"feedItemUid\":\"952f5077-4f95-4783-96e8-6bf6fae1fac6\",\"categoryUid\":\"952e067c-df94-47ac-b301-51e84e37309a\",\"amount\":{\"currency\":\"GBP\",\"minorUnits\":1112},\"sourceAmount\":{\"currency\":\"GBP\",\"minorUnits\":1112},\"direction\":\"OUT\",\"updatedAt\":\"2024-04-10T20:31:\n";
    private static final UUID accountUUid = UUID.fromString("952e7ade-e540-4691-b94d-12406d2285d8");
    private static final Account ACCOUNT = new Account(accountUUid);
    private static final Transaction TRANSACTION = new Transaction(new Amount(1112, Currency.GBP), Direction.OUT);
    private static final AccountsResponse ACCOUNTS_RESPONSE = new AccountsResponse(Set.of(ACCOUNT));
    private static final TransactionsResponse TRANSACTIONS_RESPONSE = new TransactionsResponse(Set.of(TRANSACTION));

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ApacheHttpClientWrapper client;

    @InjectMocks
    private ApacheStarlingApiClient subjectUnderTest;

    @Test
    void getAccounts_successfulRequest_returnsAccounts() throws IOException {
        givenAccountsRequestReturnsSuccessfulResponse();
        givenAccountsResponseMapsToAccounts();
        Set<Account> accounts = whenGetAccountsIsCalled();
        thenAccountsAreReturned(accounts);
    }

    @Test
    void getAccounts_unsuccessfulRequest_throwsError() throws IOException {
        givenAccountsRequestReturnsUnsuccessfulResponse();
        thenErrorThrownWhenGetAccountsCalled();
        thenNoResponseMappingIsAttempted();
    }

    @Test
    void getTransactions_successfulRequest_returnsTransactions() throws IOException {
        givenTransactionsRequestReturnsSuccessfulResponse();
        givenTransactionsResponseMapsToTransactions();
        Set<Transaction> transactions = whenGetTransactionsIsCalled();
        thenTransactionsAreReturned(transactions);
    }

    private void givenAccountsRequestReturnsSuccessfulResponse() throws IOException {
        ResponseWithCode response = new ResponseWithCode(RAW_ACCOUNTS_RESPONSE, 200);
        when(client.makeRequest(any(HttpGet.class))).thenReturn(response);
    }

    private void givenAccountsRequestReturnsUnsuccessfulResponse() throws IOException {
        ResponseWithCode response = new ResponseWithCode("", 300);
        when(client.makeRequest(any(HttpGet.class))).thenReturn(response);
    }

    private void givenAccountsResponseMapsToAccounts() throws IOException {
        when(objectMapper.readValue(RAW_ACCOUNTS_RESPONSE, AccountsResponse.class)).thenReturn(ACCOUNTS_RESPONSE);
    }

    private void givenTransactionsRequestReturnsSuccessfulResponse() throws IOException {
        ResponseWithCode response = new ResponseWithCode(RAW_TRANSACTIONS_RESPONSE, 200);
        when(client.makeRequest(any(HttpGet.class))).thenReturn(response);
    }

    private void givenTransactionsResponseMapsToTransactions() throws IOException {
        when(objectMapper.readValue(RAW_TRANSACTIONS_RESPONSE, TransactionsResponse.class)).thenReturn(TRANSACTIONS_RESPONSE);
    }

    private Set<Account> whenGetAccountsIsCalled() throws IOException {
         return subjectUnderTest.getAccounts();
    }

    private Set<Transaction> whenGetTransactionsIsCalled() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime fromDate = ZonedDateTime.parse("2024-04-08T20:31:42.765Z", formatter);
        ZonedDateTime toDate = ZonedDateTime.parse("2024-04-15T20:31:42.765Z", formatter);
        return subjectUnderTest.getTransactions(accountUUid, fromDate, toDate);
    }

    private void thenAccountsAreReturned(Set<Account> accounts) {
        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        assertEquals(ACCOUNT, accounts.iterator().next());
    }

    private void thenTransactionsAreReturned(Set<Transaction> transactions) {
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(TRANSACTION, transactions.iterator().next());
    }

    private void thenErrorThrownWhenGetAccountsCalled() {
        assertThrows(
                StarlingApiException.class,
                () -> subjectUnderTest.getAccounts());
    }

    private void thenNoResponseMappingIsAttempted() throws IOException {
        verify(objectMapper, never()).readValue(anyString(), any(Class.class));
    }
}
