package com.worsley;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.worsley.client.ApacheHttpClientWrapper;
import com.worsley.client.ApacheStarlingApiClient;
import com.worsley.client.StarlingApiClient;
import com.worsley.service.SavingsGoalsManager;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws IOException {
        /*
        NOTE_FOR_REVIEWER: I could extract this to a Factory or use a Dependency Injection framework such as Guice
        or Spring but for the sake of this tech test to keep it simple I am leaving it here.
         */
        StarlingApiClient starlingApiClient = new ApacheStarlingApiClient(new ObjectMapper(), new ApacheHttpClientWrapper());
        SavingsGoalsManager savingsGoalsManager = new SavingsGoalsManager(starlingApiClient);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        ZonedDateTime fromDate = ZonedDateTime.parse("2024-04-08T20:31:42.765Z", formatter);
        ZonedDateTime toDate = ZonedDateTime.parse("2024-04-15T20:31:42.765Z", formatter);
        UUID savingsGoalUui = UUID.fromString("95481e49-8f85-43f5-9c29-c9639f17eccf");
        savingsGoalsManager.putRoundupOfTransactionsIntoGoal(fromDate, toDate, savingsGoalUui);
    }
}