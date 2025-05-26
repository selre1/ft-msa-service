package com.ftcompany.orderservice;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDto {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String accountId;
}
