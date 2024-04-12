package com.worsley.client;

import com.worsley.dto.Account;

import java.io.IOException;
import java.util.Set;

public interface StarlingApiClient {

    Set<Account> getAccounts() throws IOException;
}
