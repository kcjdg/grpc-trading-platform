package me.kcj.user.exceptions;

import lombok.RequiredArgsConstructor;

public class UnknownUserException extends RuntimeException {

    private static final String MESSAGE = "User [id=%] is not found";

    public UnknownUserException(Integer userId) {
        super(MESSAGE.formatted(userId));
    }
}
