package com.worsley.client;

import org.junit.jupiter.api.Test;


public class AccountsClientIntegrationTest {

    @Test
    void validRequest_TODONAME_returnsAccounts() throws Exception {
        AccountsClient accountsClient = new AccountsClient();
        accountsClient.getAccounts();
    }
}
