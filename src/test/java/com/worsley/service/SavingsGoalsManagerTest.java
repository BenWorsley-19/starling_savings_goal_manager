package com.worsley.service;

import com.worsley.client.StarlingApiClient;
import com.worsley.client.exception.StarlingApiException;
import com.worsley.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private Transaction outboundTransaction2;
    private Transaction outboundTransaction3;
    private Transaction outboundTransactionWithNoRoundUp;
    private Transaction inboundTransaction1;

    @BeforeEach
    void setUp() {
        goalUuid = UUID.randomUUID();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        fromDate = ZonedDateTime.parse("2024-04-08T20:31:42.765Z", formatter);
        toDate = ZonedDateTime.parse("2024-04-15T20:31:42.765Z", formatter);
        accountUuid = UUID.randomUUID();
        account1 = new Account(accountUuid);
        outboundTransaction1 = new Transaction(new Amount(87, Currency.GBP), Direction.OUT);
        outboundTransaction2 = new Transaction(new Amount(435, Currency.GBP), Direction.OUT);
        outboundTransaction3 = new Transaction(new Amount(520, Currency.GBP), Direction.OUT);
        outboundTransactionWithNoRoundUp = new Transaction(new Amount(100, Currency.GBP), Direction.OUT);
        inboundTransaction1 = new Transaction(new Amount(20, Currency.EUR), Direction.IN);
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_singleAccountSingleTransaction_updatesGoal() throws IOException {
        givenASingleAccount();
        givenASingleOutboundTransaction();
        whenPutRoundupOfTransactionsIntoGoalCalled();
        thenGoalUpdatedForSingleOutboundTransaction();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_multipleOutboundTransactions_updatesGoalMultipleTimes() throws IOException {
        givenASingleAccount();
        givenMultipleOutboundTransactions();
        whenPutRoundupOfTransactionsIntoGoalCalled();
        thenGoalUpdatedForMultipleOutboundTransactions();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_inboundTransactionsOnly_doesNoteUpdateGoal() throws IOException {
        givenASingleAccount();
        givenASingleInboundTransaction();
        whenPutRoundupOfTransactionsIntoGoalCalled();
        thenGoalNotUpdated();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_inAndOutTransactions_updatesGoal() throws IOException {
        givenASingleAccount();
        givenASingleInboundTransaction();
        givenASingleOutboundTransaction();
        whenPutRoundupOfTransactionsIntoGoalCalled();
        thenGoalUpdatedForSingleOutboundTransaction();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_transactionsWithNothingToRoundUp_doesNotUpdateGoal() throws IOException {
        givenASingleAccount();
        givenAnOutboundTransactionWithNoRoundUp();
        whenPutRoundupOfTransactionsIntoGoalCalled();
        thenGoalNotUpdated();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_noAccounts_doesNotUpdateGoal() throws IOException {
        givenNoAccounts();
        whenPutRoundupOfTransactionsIntoGoalCalled();
        thenGoalNotUpdated();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_noTransactions_doesNotUpdateGoal() throws IOException {
        givenASingleAccount();
        givenNoTransactions();
        whenPutRoundupOfTransactionsIntoGoalCalled();
        thenGoalNotUpdated();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_errorsWhenGetAccounts_throwsError() throws IOException {
        givenErrorsWhenGetAccounts();
        thenErrorThrownWhenPutRoundupOfTransactionsIntoGoalCalled();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_errorsWhenGetTransactions_throwsError() throws IOException {
        givenASingleAccount();
        givenErrorsWhenGetTransactions();
        thenErrorThrownWhenPutRoundupOfTransactionsIntoGoalCalled();
    }

    @Test
    void putRoundupOfTransactionsIntoGoal_errorsWhenUpdateGoal_throwsError() throws IOException {
        givenASingleAccount();
        givenASingleOutboundTransaction();
        givenErrorsWhenUpdateGoal();
        thenErrorThrownWhenPutRoundupOfTransactionsIntoGoalCalled();
    }

    private void givenASingleAccount() throws IOException {
        when(apiClient.getAccounts()).thenReturn(Set.of(account1));
    }

    private void givenNoAccounts() throws IOException {
        when(apiClient.getAccounts()).thenReturn(Set.of());
    }

    private void givenASingleOutboundTransaction() throws IOException {
        when(apiClient.getTransactions(accountUuid, fromDate, toDate)).thenReturn(Set.of(outboundTransaction1));
    }

    private void givenMultipleOutboundTransactions() throws IOException {
        when(apiClient.getTransactions(accountUuid, fromDate, toDate)).thenReturn(Set.of(outboundTransaction1, outboundTransaction2, outboundTransaction3));
    }

    private void givenAnOutboundTransactionWithNoRoundUp() throws IOException {
        when(apiClient.getTransactions(accountUuid, fromDate, toDate)).thenReturn(Set.of(outboundTransactionWithNoRoundUp));
    }

    private void givenASingleInboundTransaction() throws IOException {
        when(apiClient.getTransactions(accountUuid, fromDate, toDate)).thenReturn(Set.of(inboundTransaction1));
    }

    private void givenNoTransactions() throws IOException {
        when(apiClient.getTransactions(accountUuid, fromDate, toDate)).thenReturn(Set.of());
    }

    private void givenErrorsWhenGetAccounts() throws IOException {
        when(apiClient.getAccounts()).thenThrow(new StarlingApiException("Error"));
    }

    private void givenErrorsWhenGetTransactions() throws IOException {
        when(apiClient.getTransactions(accountUuid, fromDate, toDate)).thenThrow(new StarlingApiException("Error"));
    }

    private void givenErrorsWhenUpdateGoal() throws IOException {
        doThrow(new StarlingApiException("Error")).when(apiClient).addMoneyToSavingsGoal(any(UUID.class), any(UUID.class), any(Integer.class), any(Currency.class));
    }

    private void whenPutRoundupOfTransactionsIntoGoalCalled() throws IOException {
        subjectUnderTest.putRoundupOfTransactionsIntoGoal(fromDate, toDate, goalUuid);
    }

    private void thenGoalUpdatedForMultipleOutboundTransactions() throws IOException {
        ArgumentCaptor<Integer> argument = ArgumentCaptor.forClass(Integer.class);
        verify(apiClient, times(3)).addMoneyToSavingsGoal(any(UUID.class), any(UUID.class), argument.capture(), any(Currency.class));
        List<Integer> values = argument.getAllValues();
        assertTrue(values.contains(65));
        assertTrue(values.contains(80));
        assertTrue(values.contains(13));
    }

    private void thenGoalUpdatedForSingleOutboundTransaction() throws IOException {
        verify(apiClient, times(1)).addMoneyToSavingsGoal(accountUuid, goalUuid, 13, Currency.GBP);
    }

    private void thenGoalNotUpdated() throws IOException {
        verify(apiClient, never()).addMoneyToSavingsGoal(any(), any(), anyInt(), any());
    }

    private void thenErrorThrownWhenPutRoundupOfTransactionsIntoGoalCalled() {
        assertThrows(
            StarlingApiException.class,
            () -> subjectUnderTest.putRoundupOfTransactionsIntoGoal(fromDate, toDate, goalUuid));
    }
}
