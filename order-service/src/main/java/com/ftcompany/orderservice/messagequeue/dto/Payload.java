package com.ftcompany.orderservice.messagequeue.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payload {
    private String order_id;
    private String account_id;
    private String product_id;
    private Integer qty;

    private Integer unit_price;

    private Integer total_price;
}
