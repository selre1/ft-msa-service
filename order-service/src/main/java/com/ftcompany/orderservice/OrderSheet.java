package com.ftcompany.orderservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class OrderSheet {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String productId;

    private Integer qty;

    private Integer unitPrice;

    private Integer totalPrice;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false, unique = true)
    private String orderId;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
