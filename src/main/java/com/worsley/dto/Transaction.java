package com.worsley.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Transaction {

    private Currency currency;
    private int amountInPennies;
    private final Direction direction;

    public Transaction(@JsonProperty("direction") Direction direction) {
        this.direction = direction;
    }

    /*
    NOTE_FOR_REVIEWER: I feel there is a better way to handle this and I prefer to keep dto classes immutable but
    for the sake of time I can devote to this tech test I have gone with this and moved forward.
     */
    @SuppressWarnings("unchecked")
    @JsonProperty("amount")
    private void unpackNested(Map<String,Object> amount) {
        this.amountInPennies = (Integer) amount.get("minorUnits");
        this.currency = Currency.valueOf((String)amount.get("currency"));
    }

    @JsonProperty("amount.currency")
    public Currency currency() {
        return currency;
    }

    public long amountInPennies() {
        return amountInPennies;
    }

    public Direction direction() {
        return direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Transaction) obj;
        return Objects.equals(this.currency, that.currency) &&
                this.amountInPennies == that.amountInPennies &&
                Objects.equals(this.direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, amountInPennies, direction);
    }

    @Override
    public String toString() {
        return "Transaction[" +
                "currency=" + currency + ", " +
                "amountInPennies=" + amountInPennies + ", " +
                "direction=" + direction + ']';
    }
}
