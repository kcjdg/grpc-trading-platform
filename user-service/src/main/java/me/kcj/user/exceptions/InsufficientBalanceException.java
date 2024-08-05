package me.kcj.user.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    private static final String MESSAGE = "User [id=%d] does not have enough fund to complete transaction";

    public InsufficientBalanceException(Integer userid) {
        super(MESSAGE.formatted(userid));
    }
}
