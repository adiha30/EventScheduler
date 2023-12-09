package com.adiha.EventScheduler.expections;

import org.springframework.security.core.AuthenticationException;

public class UserNotAuthorized extends AuthenticationException {

    public UserNotAuthorized(final String msg) {
        super(msg);
    }

}