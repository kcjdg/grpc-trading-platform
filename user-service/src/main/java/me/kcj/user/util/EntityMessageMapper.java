package me.kcj.user.util;

import me.kcj.user.Holding;
import me.kcj.user.StockTradeRequest;
import me.kcj.user.StockTradeResponse;
import me.kcj.user.UserInformation;
import me.kcj.user.entity.PortfolioItem;
import me.kcj.user.entity.User;

import java.util.List;

public class EntityMessageMapper {

    public static UserInformation toUserInformation(User user, List<PortfolioItem> items) {
        var holdings = items.stream()
                .map(i -> Holding.newBuilder()
                        .setTicker(i.getTicker())
                        .setQuantity(i.getQuantity())
                        .build())
                .toList();
        return UserInformation.newBuilder()
                .setUserId(user.getId())
                .setName(user.getName())
                .setBalance(user.getBalance())
                .addAllHoldings(holdings)
                .build();

    }

    public static PortfolioItem toPortfolioItem(StockTradeRequest request) {
        var item = new PortfolioItem();
        item.setUserId(request.getUserId());
        item.setTicker(request.getTicker());
        item.setQuantity(request.getQuantity());
        return item;
    }


    public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, int balance) {
        return StockTradeResponse.newBuilder()
                .setUserId(request.getUserId())
                .setPrice(request.getPrice())
                .setTicker(request.getTicker())
                .setQuantity(request.getQuantity())
                .setAction(request.getAction())
                .setTotalPrice(request.getPrice() * request.getQuantity())
                .setBalance(balance)
                .build();

    }
}
