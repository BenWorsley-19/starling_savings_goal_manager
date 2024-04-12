package com.worsley.client.response;

/*
NOTE_FOR_REVIEWER: I could have added https://www.baeldung.com/java-tuples but decided to just use this
 */
public record ResponseWithCode(String response, int statusCode){}