package com.worsley;


import com.worsley.client.TransactionsClient;
import java.net.http.HttpClient;

public class Main {

    public static void main(String[] args) {
        /*
        NOTE_FOR_REVIEWER: using this http client over apaches HttpClient as since it's addition to java 11 it means
        no additional dependencies need be added to the project.
        I could provide a wrapper around this to allow us to more easily swap between different HttpClient
        implementations, but I think it unlikely we would change this so just going to pass in this HttpClient directly
         */
        HttpClient httpClient = HttpClient.newHttpClient();
        /*
        NOTE_FOR_REVIEWER: I could extract this to a Factory or use a Dependency Injection framework such as Guice
        or Spring but for the sake of this tech test to keep it simple I am leaving it here.
         */
        TransactionsClient transactionRetriever = new TransactionsClient(httpClient);
//        transactionRetriever.getTransactionsInWeek(); // TODO
    }
}