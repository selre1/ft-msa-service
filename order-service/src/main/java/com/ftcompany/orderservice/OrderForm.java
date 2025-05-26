package com.ftcompany.orderservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderForm {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
