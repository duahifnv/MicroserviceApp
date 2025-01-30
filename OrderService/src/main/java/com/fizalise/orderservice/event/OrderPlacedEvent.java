package com.fizalise.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// TODO: 30.01.2025 Try record instead of class
public class OrderPlacedEvent {
    private String orderNumber;
    private String email;
}
