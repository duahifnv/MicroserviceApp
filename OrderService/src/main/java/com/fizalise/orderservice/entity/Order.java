package com.fizalise.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "order_number")
    private String orderNumber;
    @Basic
    @Column(name = "sku_code")
    private String skuCode;
    @Basic
    @Column(name = "price")
    private BigDecimal price;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
}
