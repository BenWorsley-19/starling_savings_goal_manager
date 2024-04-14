package com.worsley.service;


import com.worsley.client.StarlingApiClient;
import com.worsley.dto.Account;
import com.worsley.dto.Direction;
import com.worsley.dto.Transaction;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

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
     * // TODO
     */
    public void putRoundupOfTransactionsIntoGoal(ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp, UUID savingsGoalUuid) throws IOException {
        Set<Account> accounts = starlingApiClient.getAccounts();
        for (Account account : accounts) {
            Set<Transaction> transactions = starlingApiClient.getTransactions(account.accountUid(), minTransactionTimestamp, maxTransactionTimestamp);
            for (Transaction transaction : transactions) {
                if (transaction.direction() != Direction.OUT) {
                    continue;
                }
                int penceToNearestPound = penceToNearestPound(transaction.amount().minorUnits());
                if (penceToNearestPound != 0) {
                    starlingApiClient.addMoneyToSavingsGoal(account.accountUid(), savingsGoalUuid, penceToNearestPound, transaction.amount().currency());
                }
            }
        }
    }

    /*
    NOTE_FOR_REVIEWER: I did note there may be a way to this through the API but assumed you wanted me to implement this
    so I have gone with this but generally I would pass off to the API if possible.
     */
    private int penceToNearestPound(int pence) {
        int remainder = pence % 100;
        return remainder == 0 ? 0 : 100 - remainder;
    }
}
