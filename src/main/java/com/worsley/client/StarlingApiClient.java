package com.worsley.client;

import com.worsley.dto.Account;
import com.worsley.dto.Transaction;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Set;

public interface StarlingApiClient {

    Set<Account> getAccounts() throws IOException;
    Set<Transaction>  getTransations(String accountUid, ZonedDateTime minTransactionTimestamp, ZonedDateTime maxTransactionTimestamp) throws IOException;
}
