package com.worsley.client;

import com.worsley.client.response.StarlingResponse;

import java.io.IOException;

public interface StarlingApiClient {

    <T> T makeGetRequest(String relativePath, Class<T> responseClass) throws IOException;
    <T> T makePutRequest(String relativePath, Class<T> responseClass) throws IOException;
}
