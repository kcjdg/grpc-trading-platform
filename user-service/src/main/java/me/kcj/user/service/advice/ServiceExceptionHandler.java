package me.kcj.user.service.advice;

import io.grpc.Status;
import me.kcj.user.exceptions.InsufficientBalanceException;
import me.kcj.user.exceptions.InsufficientSharesException;
import me.kcj.user.exceptions.UnknownTickerException;
import me.kcj.user.exceptions.UnknownUserException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcAdvice
public class ServiceExceptionHandler {

    @GrpcExceptionHandler(UnknownTickerException.class)
    public Status handleInvalidArguments(UnknownTickerException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(UnknownUserException.class)
    public Status handleUnknownEntities(UnknownUserException e) {
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler({InsufficientBalanceException.class, InsufficientSharesException.class})
    public Status handlePreConditionFailures(Exception e) {
        return Status.FAILED_PRECONDITION.withDescription(e.getMessage());
    }


}
