package me.kcj.user.repository;

import me.kcj.common.Ticker;
import me.kcj.user.entity.PortfolioItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioItemRepository extends CrudRepository<PortfolioItem, Integer> {

    List<PortfolioItem> findAllByUserId(Integer userid);

    Optional<PortfolioItem> findByUserIdAndTicker(Integer userId, Ticker ticker);

}
