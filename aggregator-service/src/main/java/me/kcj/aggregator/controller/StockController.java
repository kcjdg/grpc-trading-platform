package me.kcj.aggregator.controller;


import lombok.RequiredArgsConstructor;
import me.kcj.aggregator.service.PriceUpdateListener;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private PriceUpdateListener priceUpdateListener;

    @GetMapping(value = "updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter priceUpdates(){
        return priceUpdateListener.createEmitter();
    }
}
