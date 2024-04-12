package com.worsley.service;

import com.worsley.client.AccountsClient;
import com.worsley.client.SavingsGoalsClient;
import com.worsley.client.TransactionsClient;

public class SavingsGoalsManager {

    private final TransactionsClient transactionsClient;
    private final AccountsClient accountsClient;
    private final SavingsGoalsClient savingsGoalsClient;

    public SavingsGoalsManager(TransactionsClient transactionsClient, AccountsClient accountsClient, SavingsGoalsClient savingsGoalsClient) {
        this.transactionsClient = transactionsClient;
        this.accountsClient = accountsClient;
        this.savingsGoalsClient = savingsGoalsClient;
    }

    /**
     * Rounds up the transactions in the given week and adds to them to the specified Savings Goal
     * such that with spending of £4.35, £5.20 and £0.87, then £1.58 would be added to the
     * Savings Goal.
     *
     * @param weekOfTransactions // TODO state format - maybe this needs to be from/to
     * @param savingsGoalUuid
     */
    public void putRoundupOfTransactionsIntoGoal(String weekOfTransactions, String savingsGoalUuid) {

    }
}

