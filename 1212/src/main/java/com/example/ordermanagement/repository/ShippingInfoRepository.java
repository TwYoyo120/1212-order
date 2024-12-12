package com.example.ordermanagement.repository;

import com.example.ordermanagement.model.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Long> {

    // 根據訂單ID查詢物流信息
    List<ShippingInfo> findByOrderId(Long orderId);
    
    // 根據多個訂單ID查詢物流信息
    List<ShippingInfo> findByOrderIdIn(List<Long> orderIds);

    // 根據物流狀態查詢物流信息
    List<ShippingInfo> findByStatus(String status);

    // 根據收件人姓名查詢物流信息
    List<ShippingInfo> findByRecipient(String recipient);

    // 根據訂單ID和物流狀態查詢物流信息
    List<ShippingInfo> findByOrderIdAndStatus(Long orderId, String status);

    // 根據收件人姓名進行模糊查詢
    List<ShippingInfo> findByRecipientContaining(String recipient);
    
    
}