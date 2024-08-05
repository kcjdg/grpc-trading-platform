package me.kcj.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import me.kcj.common.Ticker;

@Entity
@Data
public class PortfolioItem {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "customer_id")
    private Integer userId;
    private Ticker ticker;
    private Integer quantity;

}
