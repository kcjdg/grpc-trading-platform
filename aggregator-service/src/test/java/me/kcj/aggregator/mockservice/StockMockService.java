package me.kcj.aggregator.mockservice;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import me.kcj.common.Ticker;
import me.kcj.stock.PriceUpdate;
import me.kcj.stock.StockPriceRequest;
import me.kcj.stock.StockPriceResponse;
import me.kcj.stock.StockServiceGrpc;

public class StockMockService extends StockServiceGrpc.StockServiceImplBase {
    @Override
    public void getStockPrice(StockPriceRequest request, StreamObserver<StockPriceResponse> responseObserver) {
        var response = StockPriceResponse.newBuilder().setPrice(15).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPriceUpdates(Empty request, StreamObserver<PriceUpdate> responseObserver) {
        for (int i = 1; i <5 ; i++) {
            var priceUpdate = PriceUpdate.newBuilder().setPrice(i).setTicker(Ticker.AMAZON).build();
            responseObserver.onNext(priceUpdate);
        }
        responseObserver.onCompleted();
    }
}
