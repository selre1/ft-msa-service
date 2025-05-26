package com.ftcompany.catalogservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatalogForm {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String orderID;
    private String userId;
}
