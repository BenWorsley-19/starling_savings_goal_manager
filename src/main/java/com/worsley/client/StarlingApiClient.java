package com.worsley.client;

import com.worsley.dto.Account;
import com.worsley.dto.Currency;
import com.worsley.dto.Transaction;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * A client for the Starling Bank API
 */
public interface StarlingApiClient {

    /**
     * Get all accounts for the user
     *
     * @return a set of accounts
     */
    Set<Account> getAccounts() throws IOException;
    /**
     * Get all transactions for the account within the given time range
     *
     * @param accountUid the account to get transactions for
     * @param minTransactionTimestamp the earliest transaction timestamp to include
     * @param maxTransactionTimestamp the latest transaction timestamp to include
     * @return a set of transactions
     */
    Set<Transaction> getTransactions(UUID accountUid, ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) throws IOException;
    /**
     * Add money to a savings goal
     *
     * @param accountUid the account to add money to
     * @param savingsGoalUuid the savings goal to add money to
     * @param amountInPennies the amount to add in pennies
     * @param currency the currency of the amount
     */
    void addMoneyToSavingsGoal(UUID accountUid, UUID savingsGoalUuid, int amountInPennies, Currency currency) throws IOException;
}
