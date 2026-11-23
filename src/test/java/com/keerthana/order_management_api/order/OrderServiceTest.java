package com.keerthana.order_management_api.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService service;

    @Test
    void createCalculatesTotalPriceAndDefaultsToCreated() {
        when(repository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        OrderResponse response = service.create(
                new CreateOrderRequest("Asha", "Laptop", 2, new BigDecimal("899.99")));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.totalPrice()).isEqualByComparingTo("1799.98");
        assertThat(response.status()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    void findByIdThrowsWhenOrderMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order 99 not found");
    }

    @Test
    void updateStatusChangesStatus() {
        Order order = sampleOrder();
        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(repository.save(order)).thenReturn(order);

        OrderResponse response = service.updateStatus(1L, OrderStatus.SHIPPED);

        assertThat(response.status()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    void deleteRemovesExistingOrder() {
        Order order = sampleOrder();
        when(repository.findById(1L)).thenReturn(Optional.of(order));

        service.delete(1L);

        verify(repository).delete(order);
    }

    @Test
    void findAllFiltersByStatusWhenProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findByStatus(OrderStatus.PAID, pageable))
                .thenReturn(new PageImpl<>(List.of(sampleOrder()), pageable, 1));

        PageResponse<OrderResponse> result = service.findAll(OrderStatus.PAID, pageable);

        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.content()).hasSize(1);
        verify(repository, never()).findAll(any(Pageable.class));
    }

    private Order sampleOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Asha");
        order.setProductName("Laptop");
        order.setQuantity(2);
        order.setUnitPrice(new BigDecimal("899.99"));
        return order;
    }
}