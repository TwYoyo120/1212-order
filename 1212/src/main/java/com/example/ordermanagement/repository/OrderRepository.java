package com.example.ordermanagement.repository;

import com.example.ordermanagement.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    // 根據訂單編號查詢訂單
    Order findByOrderNumber(String orderNumber);
    
    List<Order> findByBuyerId(Long buyerId);

    // 其他根據buyerId, sellerId的方法在多賣家情境下已不再適合直接放在Order上
    // 若需要根據sellerId搜尋訂單，需透過Specification join到items表進行篩選。
    
    
}
