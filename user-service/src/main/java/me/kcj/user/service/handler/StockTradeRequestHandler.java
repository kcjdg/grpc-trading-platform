package me.kcj.user.service.handler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.kcj.common.Ticker;
import me.kcj.user.StockTradeRequest;
import me.kcj.user.StockTradeResponse;
import me.kcj.user.exceptions.InsufficientBalanceException;
import me.kcj.user.exceptions.InsufficientSharesException;
import me.kcj.user.exceptions.UnknownTickerException;
import me.kcj.user.exceptions.UnknownUserException;
import me.kcj.user.repository.PortfolioItemRepository;
import me.kcj.user.repository.UserRepository;
import me.kcj.user.util.EntityMessageMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockTradeRequestHandler {

    private final UserRepository userRepository;
    private final PortfolioItemRepository portfolioItemRepository;


    @Transactional
    public StockTradeResponse buyStock(StockTradeRequest request) {
        //validate
        validateTicker(request.getTicker());
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UnknownUserException(request.getUserId()));

        var totalPrice = request.getQuantity() * request.getPrice();
        this.validateUserBalance(user.getId(), user.getBalance(), totalPrice);

        //valid request
        user.setBalance(user.getBalance() - totalPrice);
        this.portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getId() + request.getQuantity()),
                        () -> this.portfolioItemRepository.save(EntityMessageMapper.toPortfolioItem(request))

                );
        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());
    }

    @Transactional
    public StockTradeResponse sellStock(StockTradeRequest request) {
        //validate
        validateTicker(request.getTicker());
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UnknownUserException(request.getUserId()));


        var portfolioItem = this.portfolioItemRepository.findByUserIdAndTicker(user.getId(), request.getTicker())
                .filter(pi -> pi.getQuantity() >= request.getQuantity())
                .orElseThrow(() -> new InsufficientSharesException(user.getId()));

        //valid request
        var totalPrice = request.getQuantity() * request.getPrice();
        user.setBalance(user.getBalance() + totalPrice);
        portfolioItem.setQuantity(portfolioItem.getQuantity() - request.getQuantity());
        return EntityMessageMapper.toStockTradeResponse(request, user.getBalance());
    }

    private void validateTicker(Ticker ticker) {
        if (Ticker.UNKNOWN.equals(ticker)) {
            throw new UnknownTickerException();
        }
    }

    private void validateUserBalance(Integer userId, Integer userBalance, Integer totalPrice) {
        if (totalPrice > userBalance) {
            throw new InsufficientBalanceException(userId);
        }
    }
}
