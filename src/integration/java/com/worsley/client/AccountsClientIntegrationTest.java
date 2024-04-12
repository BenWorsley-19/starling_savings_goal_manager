package com.worsley.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;


public class AccountsClientIntegrationTest {

    @Test
    void validRequest_TODONAME_returnsAccounts() throws Exception {
        StarlingApiClient starlingApiClient = new ApacheStarlingApiClient(new ObjectMapper(), new ApacheHttpClientWrapper());
        starlingApiClient.getAccounts();
    }
}
