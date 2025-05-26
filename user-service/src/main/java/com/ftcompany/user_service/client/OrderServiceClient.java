package com.ftcompany.user_service.client;

import com.ftcompany.user_service.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/order/{accountId}/order")
    List<ResponseOrder> getOrders(@PathVariable String accountId);
}
