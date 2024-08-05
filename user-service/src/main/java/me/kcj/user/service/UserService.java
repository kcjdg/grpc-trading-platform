package me.kcj.user.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import me.kcj.user.*;
import me.kcj.user.service.handler.UserInformationHandler;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserInformationHandler userInformationHandler;

    @Override
    public void getUserInformation(UserInformationRequest request, StreamObserver<UserInformation> responseObserver) {
        var userInformation = userInformationHandler.getUserInformation(request);
        responseObserver.onNext(userInformation);
        responseObserver.onCompleted();
    }

    @Override
    public void tradeStock(StockTradeRequest request, StreamObserver<StockTradeResponse> responseObserver) {
    }
}
