package com.worsley.client;

import com.worsley.dto.Account;
import com.worsley.dto.Currency;
import com.worsley.dto.Transaction;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public interface StarlingApiClient {

    Set<Account> getAccounts() throws IOException;
    Set<Transaction> getTransactions(UUID accountUid, ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) throws IOException;
    void addMoneyToSavingsGoal(UUID accountUid, UUID savingsGoalUuid, int amountInPennies, Currency currency) throws IOException;
}
