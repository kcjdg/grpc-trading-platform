package me.kcj.aggregator.service;

import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.kcj.aggregator.dto.PriceUpdateDto;
import me.kcj.stock.PriceUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class PriceUpdateListener implements StreamObserver<PriceUpdate> {

    private final Set<SseEmitter> emitters = Collections.synchronizedSet(new HashSet<>());
    private final long sseTimeout;

    public PriceUpdateListener(@Value("${sse.timeout:300000}") long sseTimeout) {
        this.sseTimeout = sseTimeout;
    }

    public SseEmitter createEmitter(){
        var emitter = new SseEmitter(this.sseTimeout);
        emitters.add(emitter);
        emitter.onTimeout(()->{
            emitters.remove(emitter);
        });
        emitter.onError(ex-> emitters.remove(emitter));
        return emitter;
    }

    @Override
    public void onCompleted() {
        emitters.forEach(ResponseBodyEmitter::complete);
        emitters.clear();
    }

    @Override
    public void onNext(PriceUpdate priceUpdate) {
        var dto = new PriceUpdateDto(priceUpdate.getTicker().toString(), priceUpdate.getPrice());
        emitters.removeIf(e->!send(e, dto));

    }

    @Override
    public void onError(Throwable throwable) {
        log.error("streaming error", throwable);
        emitters.forEach(e->e.completeWithError(throwable));
        emitters.clear();
    }

    private boolean send(SseEmitter emitter, Object o){
        try {
            emitter.send(o);
            return true;
        }catch (IOException e){
            log.warn("sse error {}", e.getMessage());
        }
        return false;
    }
}
