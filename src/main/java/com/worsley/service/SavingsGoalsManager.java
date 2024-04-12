package com.worsley.service;


import com.worsley.client.StarlingApiClient;

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
    public void putRoundupOfTransactionsIntoGoal(String weekOfTransactions, String savingsGoalUuid) {

    }
}

