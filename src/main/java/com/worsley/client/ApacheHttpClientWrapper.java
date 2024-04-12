package com.worsley.client;

import com.worsley.client.response.ResponseWithCode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApacheHttpClientWrapper {

    public ResponseWithCode makeRequest(HttpRequestBase request) throws IOException {
        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(request)) {
            String stringResponse = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            return new ResponseWithCode(stringResponse, statusCode);
        }
    }
}


