package com.keerthana.order_management_api.order;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        String customerName,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        OrderStatus status,
        Instant createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getProductName(),
                order.getQuantity(),
                order.getUnitPrice(),
                order.getUnitPrice().multiply(BigDecimal.valueOf(order.getQuantity())),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}