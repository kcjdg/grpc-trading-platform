package me.kcj.aggregator.service;

import me.kcj.stock.StockPriceRequest;
import me.kcj.stock.StockServiceGrpc;
import me.kcj.user.StockTradeRequest;
import me.kcj.user.StockTradeResponse;
import me.kcj.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class TradeService {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    @GrpcClient("stock-service")
    private StockServiceGrpc.StockServiceBlockingStub stockClient;

    public StockTradeResponse trade(StockTradeRequest request) {
        var priceRequest = StockPriceRequest.newBuilder()
                .setTicker(request.getTicker())
                .build();
        var priceResponse = stockClient.getStockPrice(priceRequest);
        var tradeRequest = request.toBuilder().setPrice(priceResponse.getPrice()).build();
        return userClient.tradeStock(tradeRequest);
    }
}
