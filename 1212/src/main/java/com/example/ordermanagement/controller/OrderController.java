package com.example.ordermanagement.controller;

import com.example.ordermanagement.model.Order;
import com.example.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 映射到訂單管理頁面
    @GetMapping("/order-management")
    public String adminOrderManagement() {
        return "adminOrderManagement"; 
    }

    
    @GetMapping("/memberPurchase")
    public String memberPurchase() {
        return "memberPurchase";
    }

    // 建立新訂單（POST）
    @ResponseBody
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order created = orderService.createOrder(order);
        return ResponseEntity.ok(created);
    }

    // 獲取訂單列表，支持多種篩選條件和分頁，並返回符合前端需求的結構
    @ResponseBody
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders(
            @RequestParam(required = false) Long buyerId,
            @RequestParam(required = false) Long sellerId,
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = orderService.getOrdersByFilters(buyerId, sellerId, orderNumber, status, startDate, endDate, PageRequest.of(page - 1, size));
        return ResponseEntity.ok(Map.of(
            "content", result.getContent(),
            "totalPages", result.getTotalPages(),
            "currentPage", result.getNumber() + 1
        ));
    }

    // 根據訂單編號獲取訂單詳情
    @ResponseBody
    @GetMapping("/{orderNumber}")
    public ResponseEntity<Order> getOrderByOrderNumber(@PathVariable String orderNumber) {
        Optional<Order> orderOpt = orderService.getOrderByOrderNumber(orderNumber);
        return orderOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 更新訂單詳情(以訂單編號)
    @ResponseBody
    @PutMapping("/{orderNumber}")
    public ResponseEntity<?> updateOrderDetails(
            @PathVariable String orderNumber,
            @RequestBody Map<String, Object> updatedFields) {
        try {
            boolean updated = orderService.updateOrderDetails(
                    orderNumber,
                    (String) updatedFields.get("paymentStatus"),
                    (String) updatedFields.get("shippingStatus"),
                    (String) updatedFields.get("status")
            );
            return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 棄單功能(以訂單編號)
    @ResponseBody
    @PostMapping("/{orderNumber}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderNumber) {
        try {
            boolean canceled = orderService.cancelOrder(orderNumber);
            return canceled ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
