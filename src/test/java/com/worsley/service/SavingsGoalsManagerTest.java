package com.worsley.service;

import com.worsley.client.StarlingApiClient;
import com.worsley.dto.*;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SavingsGoalsManagerTest {

    @Mock
    private StarlingApiClient apiClient;

    @InjectMocks
    private SavingsGoalsManager subjectUnderTest;

    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private UUID goalUuid;
    private UUID accountUuid;
    private Account account1;
    private Transaction outboundTransaction1;
    private Transaction inboundTransaction1;

    @BeforeEach
    void setUp() {
        goalUuid = UUID.randomUUID();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        fromDate = ZonedDateTime.parse("2024-04-08T20:31:42.765Z", formatter);
        toDate = ZonedDateTime.parse("2024-04-15T20:31:42.765Z", formatter);
        accountUuid = UUID.randomUUID();
        account1 = new Account(accountUuid);
        outboundTransaction1 = new Transaction(new Amount(10, Currency.GBP), Direction.OUT);
        inboundTransaction1 = new Transaction(new Amount(20, Currency.EUR), Direction.OUT);
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_singleAccountSingleTransaction_updatesGoal() throws IOException {
        givenASingleAccountAndSingleTransaction();
        whenPutRoundupOfTransactionsIntoGoalCalled();
        thenGoalUpdatedForSingleOutboundTransaction();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_inboundTransactionsOnly_doesNoteUpdateGoal() {

    }

    @Test
    void putRoundupOfTransactionsIntoGoal_outboundTransactionsOnly_updatesGoal() {

    }

    @Test
    void putRoundupOfTransactionsIntoGoal_inAndOutTransactions_updatesGoal() {

    }

    @Test
    void putRoundupOfTransactionsIntoGoal_transactionsWithNothingToRoundUp_doesNotUpdateGoal() {

    }

    @Test
    void putRoundupOfTransactionsIntoGoal_noAccounts_doesNotUpdateGoal() {

    }

    @Test
    void putRoundupOfTransactionsIntoGoal_noTransactions_doesNotUpdateGoal() {

    }

    private void givenASingleAccountAndSingleTransaction() throws IOException {
        when(apiClient.getAccounts()).thenReturn(Set.of(account1));
        when(apiClient.getTransactions(accountUuid, fromDate, toDate)).thenReturn(Set.of(outboundTransaction1));
    }

    private void whenPutRoundupOfTransactionsIntoGoalCalled() throws IOException {
        subjectUnderTest.putRoundupOfTransactionsIntoGoal(fromDate, toDate, goalUuid);
    }

    private void thenGoalUpdatedForSingleOutboundTransaction() throws IOException {
        verify(apiClient, times(1)).addMoneyToSavingsGoal(accountUuid, goalUuid, 90, Currency.GBP);
    }
}
