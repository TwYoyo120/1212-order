package com.example.ordermanagement.service;

import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.model.ShippingInfo;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.repository.ShippingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    // 建立新訂單並自動產生訂單編號
    public Order createOrder(Order order) {
        order.setStatus("Pending");
        order.setOrderDate(LocalDateTime.now());
        order.setOrderNumber("TEMP");
        order = orderRepository.save(order);
        order.setOrderNumber("ORDER-" + order.getId());
        return orderRepository.save(order);
    }

    // 根據篩選條件查詢訂單，支持模糊查詢
    public Page<Order> getOrdersByFilters(Long buyerId, Long sellerId, String orderNumber, String status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Order> spec = Specification.where(null);

        if (orderNumber != null && !orderNumber.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("orderNumber"), "%" + orderNumber + "%"));
        }
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            spec = spec.and((root, query, cb) -> cb.between(root.get("orderDate"), startDateTime, endDateTime));
        }
        if (buyerId != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("buyerId").as(String.class), "%" + buyerId + "%"));
        }
        if (sellerId != null) {
            spec = spec.and((root, query, cb) -> {
                var itemsJoin = root.join("items");
                return cb.equal(itemsJoin.get("sellerId"), sellerId);
            });
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        return orderRepository.findAll(spec, pageable);
    }

    // 根據ID獲取訂單
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // 根據訂單編號獲取訂單
    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return Optional.ofNullable(orderRepository.findByOrderNumber(orderNumber));
    }

    // 更新訂單詳情
    public boolean updateOrderDetails(String orderNumber, String paymentStatus, String shippingStatus, String status) {
        return getOrderByOrderNumber(orderNumber).map(order -> {
            if (paymentStatus != null) order.setPaymentStatus(paymentStatus);
            if (shippingStatus != null) order.setShippingStatus(shippingStatus);
            if (status != null) order.setStatus(status);
            orderRepository.save(order);
            return true;
        }).orElse(false);
    }

    // 棄單功能
    public boolean cancelOrder(String orderNumber) {
        return getOrderByOrderNumber(orderNumber).map(order -> {
            if ("Canceled".equals(order.getStatus())) throw new IllegalStateException("訂單已被標記為棄單");
            order.setStatus("Canceled");
            orderRepository.save(order);
            List<ShippingInfo> shippingInfos = shippingInfoRepository.findByOrderId(order.getId());
            shippingInfos.forEach(info -> {
                info.setStatus("Canceled");
                shippingInfoRepository.save(info);
            });
            return true;
        }).orElse(false);
    }
}
