package me.kcj.aggregator.service;

import me.kcj.user.UserInformation;
import me.kcj.user.UserInformationRequest;
import me.kcj.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    public UserInformation getUserInformation(int userId) {
        var request = UserInformationRequest.newBuilder()
                .setUserId(userId)
                .build();

        return userClient.getUserInformation(request);
    }

}
