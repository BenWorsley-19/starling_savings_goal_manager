package com.worsley.service;


import com.worsley.client.StarlingApiClient;
import com.worsley.dto.Account;

import java.io.IOException;
import java.util.Set;

public class SavingsGoalsManager {

    private final StarlingApiClient starlingApiClient;

    public SavingsGoalsManager(StarlingApiClient starlingApiClient) {
        this.starlingApiClient = starlingApiClient;
    }

    /**
     * Rounds up the transactions in the given week and adds to them to the specified Savings Goal
     * such that with spending of £4.35, £5.20 and £0.87, then £1.58 would be added to the
     * Savings Goal.
     *
     * @param weekOfTransactions // TODO state format - maybe this needs to be from/to
     * @param savingsGoalUuid
     */
    public void putRoundupOfTransactionsIntoGoal(String weekOfTransactions, String savingsGoalUuid) throws IOException {
        Set<Account> accounts = starlingApiClient.getAccounts();
//        accounts.forEach(account -> {
//            starlingApiClient
//        }
    }
}

