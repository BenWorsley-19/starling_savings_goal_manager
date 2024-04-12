package com.worsley.client.response;

import com.worsley.dto.Account;

import java.util.Set;

public record AccountsResponse(Set<Account> accounts) {
}
