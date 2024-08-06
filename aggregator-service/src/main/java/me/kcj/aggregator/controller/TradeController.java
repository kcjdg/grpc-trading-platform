package me.kcj.aggregator.controller;

import lombok.RequiredArgsConstructor;
import me.kcj.aggregator.service.TradeService;
import me.kcj.user.StockTradeRequest;
import me.kcj.user.StockTradeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping
    public StockTradeResponse trade(@RequestBody StockTradeRequest stockTradeRequest){
        return tradeService.trade(stockTradeRequest);
    }

}
