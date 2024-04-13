package com.worsley.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.dto.Account;
import com.worsley.dto.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class StarlingApiClientIntegrationTest {

    private StarlingApiClient starlingApiClient;

    @BeforeEach
    void setUp() {
        starlingApiClient = new ApacheStarlingApiClient(new ObjectMapper(), new ApacheHttpClientWrapper());
    }

    @Test
    void getAccounts_validRequest_returnsAccounts() throws Exception {
        Set<Account> result = starlingApiClient.getAccounts();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getTransactions_validRequest_returnsAccounts() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime fromDate = ZonedDateTime.parse("2024-04-08T20:31:42.765Z", formatter);
        ZonedDateTime toDate = ZonedDateTime.parse("2024-04-15T20:31:42.765Z", formatter);

        String accountId = "952e7ade-e540-4691-b94d-12406d2285d8";
        StarlingApiClient starlingApiClient = new ApacheStarlingApiClient(new ObjectMapper(), new ApacheHttpClientWrapper());
        Set<Transaction> result = starlingApiClient.getTransations(accountId, fromDate, toDate);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
