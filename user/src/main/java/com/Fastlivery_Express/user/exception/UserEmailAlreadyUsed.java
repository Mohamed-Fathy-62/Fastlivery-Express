package com.Fastlivery_Express.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email is already in use")
public class UserEmailAlreadyUsed extends RuntimeException {
    public UserEmailAlreadyUsed(String message) {
        super(message);
    }
}
