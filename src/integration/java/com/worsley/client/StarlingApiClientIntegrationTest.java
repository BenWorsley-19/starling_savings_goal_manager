package com.worsley.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.dto.Account;
import com.worsley.dto.Currency;
import com.worsley.dto.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/*
NOTE_FOR_REVIEWER: With more time I would bulk this out with better assertions, maybe using something like MockMvc or create mock servers to
provide test data.
 */
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
    void getTransactions_validRequest_returnsTransactions() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime fromDate = ZonedDateTime.parse("2024-04-08T20:31:42.765Z", formatter);
        ZonedDateTime toDate = ZonedDateTime.parse("2024-04-15T20:31:42.765Z", formatter);

        UUID accountId = UUID.fromString("952e7ade-e540-4691-b94d-12406d2285d8");
        Set<Transaction> result = starlingApiClient.getTransactions(accountId, fromDate, toDate);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void gaddMoneyToSavingsGoal_validRequest_executesSuccessfully() throws Exception {
        UUID accountId = UUID.fromString("952e7ade-e540-4691-b94d-12406d2285d8");
        UUID savingsGoalUui = UUID.fromString("95481e49-8f85-43f5-9c29-c9639f17eccf");
        starlingApiClient.addMoneyToSavingsGoal(accountId, savingsGoalUui, 10, Currency.GBP);
    }
}
