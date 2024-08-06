package me.kcj.user;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import me.kcj.common.Ticker;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.user-service.address=in-process:integration-test"
})
public class UserServiceTest {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    @Test
    public void userInformationTest() {
        var request = UserInformationRequest.newBuilder()
                .setUserId(1)
                .build();
        var response = stub.getUserInformation(request);
        Assertions.assertEquals(10_000, response.getBalance());
        Assertions.assertEquals("Sam", response.getName());
        Assertions.assertTrue(response.getHoldingsList().isEmpty());
    }


    @Test
    public void unknowUserExceptionTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = UserInformationRequest.newBuilder()
                    .setUserId(10)
                    .build();
            stub.getUserInformation(request);
        });

        Assertions.assertEquals(Status.Code.NOT_FOUND, ex.getStatus().getCode());
    }

    @Test
    public void unknownTickerBuyTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            final var request = StockTradeRequest.newBuilder()
                    .setUserId(1)
                    .setAction(TradeAction.BUY)
                    .setQuantity(1)
                    .setPrice(1)
                    .build();
            stub.tradeStock(request);
        });

        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
    }


    @Test
    public void insufficientSharesTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            final var request = StockTradeRequest.newBuilder()
                    .setUserId(1)
                    .setAction(TradeAction.SELL)
                    .setTicker(Ticker.AMAZON)
                    .setQuantity(1000)
                    .setPrice(1)
                    .build();

            stub.tradeStock(request);
        });
        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION, ex.getStatus().getCode());
    }

    @Test
    public void insufficientBalanceTest() {
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            final var request = StockTradeRequest.newBuilder()
                    .setUserId(1)
                    .setAction(TradeAction.BUY)
                    .setTicker(Ticker.AMAZON)
                    .setQuantity(10)
                    .setPrice(10_001)
                    .build();

            stub.tradeStock(request);

        });
        Assertions.assertEquals(Status.Code.FAILED_PRECONDITION, ex.getStatus().getCode());
    }


    @Test
    public void buyAndSellTest() {
        var buyReq = StockTradeRequest.newBuilder()
                .setUserId(1)
                .setAction(TradeAction.BUY)
                .setTicker(Ticker.AMAZON)
                .setQuantity(5)
                .setPrice(100)
                .build();

        var buyResponse = stub.tradeStock(buyReq);

        //validate balance
        Assertions.assertEquals(9500, buyResponse.getBalance());

        //check holding
        var userRequest = UserInformationRequest.newBuilder().setUserId(1).build();
        var userResponse = stub.getUserInformation(userRequest);

        Assertions.assertEquals(1, userResponse.getHoldingsCount());
        Assertions.assertEquals(Ticker.AMAZON, userResponse.getHoldingsList().getFirst().getTicker());

        //sell
        var sellRequest = buyReq.toBuilder().setAction(TradeAction.SELL).setPrice(102).build();
        var sellResponse = stub.tradeStock(sellRequest);

        //validate balance
        Assertions.assertEquals(10_010, sellResponse.getBalance());
    }
}
