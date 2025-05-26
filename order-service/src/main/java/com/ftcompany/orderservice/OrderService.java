package com.ftcompany.orderservice;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderDto createOrder(OrderDto orderDto){
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());
        OrderSheet order = modelMapper.map(orderDto,OrderSheet.class);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        OrderDto newOrderDto = modelMapper.map(order,OrderDto.class);

        return newOrderDto;
    }
}
