package me.kcj.user.util;

import me.kcj.user.Holding;
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
}
