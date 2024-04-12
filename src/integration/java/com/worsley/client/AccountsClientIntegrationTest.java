package com.worsley.client;

import org.junit.jupiter.api.Test;


public class AccountsClientIntegrationTest {

    @Test
    void validRequest_TODONAME_returnsAccounts() throws Exception {
        StarlingApiClient starlingApiClient = new ApacheStarlingApiClient();
        AccountsClient accountsClient = new AccountsClient(starlingApiClient);
        accountsClient.getAccounts();
    }
}
