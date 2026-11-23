package com.keerthana.order_management_api.order;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public OrderResponse create(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setProductName(request.productName());
        order.setQuantity(request.quantity());
        order.setUnitPrice(request.unitPrice());
        return OrderResponse.from(repository.save(order));
    }

    public PageResponse<OrderResponse> findAll(OrderStatus status, Pageable pageable) {
        Page<Order> page = (status == null)
                ? repository.findAll(pageable)
                : repository.findByStatus(status, pageable);
        return PageResponse.from(page.map(OrderResponse::from));
    }

    public OrderResponse findById(Long id) {
        return OrderResponse.from(getOrder(id));
    }

    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = getOrder(id);
        order.setStatus(status);
        return OrderResponse.from(repository.save(order));
    }

    public void delete(Long id) {
        repository.delete(getOrder(id));
    }

    private Order getOrder(Long id) {
        return repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }
}