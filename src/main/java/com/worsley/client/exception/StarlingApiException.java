package com.worsley.client.exception;

/*
NOTE_FOR_REVIEWER chose unchecked exception because for the sake of this tech test if something goes wrong with the
API call we're just going to stop the app
 */
public class StarlingApiException extends RuntimeException {
    public StarlingApiException(String errorMessage) {
        super(errorMessage);
    }
}
