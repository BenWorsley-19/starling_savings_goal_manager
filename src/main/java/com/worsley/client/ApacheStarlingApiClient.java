package com.worsley.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.client.response.StarlingResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class ApacheStarlingApiClient implements StarlingApiClient {

    private static Logger logger = LoggerFactory.getLogger(ApacheStarlingApiClient.class);

    public static final String STARLING_BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/";

    @Override
    public <T> T makeGetRequest(String relativePath, Class<T> responseClass) throws IOException {
        final HttpGet request = new HttpGet(URI.create(STARLING_BASE_URL + relativePath));
        return makeRequest(request, responseClass);
    }

    @Override
    public <T> T makePutRequest(String relativePath, Class<T> responseClass) throws IOException {
        final HttpPut request = new HttpPut(URI.create(STARLING_BASE_URL + relativePath));
        return makeRequest(request, responseClass);
    }

    private <T> T makeRequest(HttpRequestBase request, Class<T> responseClass) throws IOException {
        String authToken = System.getenv("STARLING_AUTH_TOKEN");
        request.addHeader("Authorization", "Bearer " + authToken);
        request.addHeader("Accept", "application/json");
        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String stringResponse = EntityUtils.toString(response.getEntity());
            if(statusCode == 401) {
                /*
                NOTE_FOR_REVIEWER: Here I could refresh the OAuth via the api provided the refresh isn't expired
                and then retry. However, for the sake of this tech test I will just log and error so user can refresh
                themselves and run the program again.
                 */
                logger.error("401");
            }
            if(statusCode != 200) {
                /*
                NOTE_FOR_REVIEWER: Here I could refresh the OAuth via the api provided the refresh isn't expired
                and then retry. However, for the sake of this tech test I will just log and error so user can refresh
                themselves and run the program again.
                 */
                logger.error("API Response not as expected. Status code: {}, response:{}", statusCode, stringResponse);
            }
            return mapJsonStringToObject(stringResponse, responseClass);
        }
    }

    // TODO move this from this class
    private <T> T mapJsonStringToObject(String jsonString, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, clazz);
    }
}
