package com.worsley.client.response;

import com.worsley.dto.Transaction;

import java.util.Set;

public record TransactionsResponse(Set<Transaction> feedItems) {
}
