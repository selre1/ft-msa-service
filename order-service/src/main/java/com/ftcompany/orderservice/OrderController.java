package com.ftcompany.orderservice;

import com.ftcompany.orderservice.messagequeue.KafkaProducer;
import com.ftcompany.orderservice.messagequeue.OrderProducer;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @Value("${server.port}")
    private String port;

    @GetMapping("/health_check")
    public String status(){
        return String.format(String.format("order Service Port %s",port));
    }

    @PostMapping("/{accountId}/order")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable String accountId, @RequestBody OrderForm orderForm){
        log.info("before added order data");
        OrderDto orderDto = modelMapper.map(orderForm,OrderDto.class);
        orderDto.setAccountId(accountId);
        /*
        * 원래는 order 받으면 db에 바로 저장
        * */
        //OrderDto newOrder = orderService.createOrder(orderDto);
        //ResponseOrder responseOrder = modelMapper.map(newOrder, ResponseOrder.class);

        /*
        * 이제는 order 받으면 kafka로 보냄
        * */
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());
        orderService.createOrder(orderDto);
        kafkaProducer.send("example-catalog-topic",orderDto);
        //orderProducer.send("order_sheet",orderDto);

        ResponseOrder responseOrder = modelMapper.map(orderDto,ResponseOrder.class);
        log.info("after added order data");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("{accountId}/order")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable String accountId){
        log.info("before retrieve order data");
        List<OrderSheet> orderSheets = orderRepository.findByAccountId(accountId);
        List<ResponseOrder> responseOrders = new ArrayList<>();

        orderSheets.forEach(order -> {
            responseOrders.add(modelMapper.map(order,ResponseOrder.class));
        });
        log.info("add retrieve order data");

        return ResponseEntity.status(HttpStatus.OK).body(responseOrders);
    }
}

