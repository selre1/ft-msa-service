package com.ftcompany.orderservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderSheet,Long> {
    List<OrderSheet> findByAccountId(String accountId);
}
