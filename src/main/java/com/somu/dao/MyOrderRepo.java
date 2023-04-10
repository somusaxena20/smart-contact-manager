package com.somu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.somu.entity.OrderDetails;

@Repository
public interface MyOrderRepo extends JpaRepository<OrderDetails, Long> {
	
	public OrderDetails findByOrderId(String orderId); 
}
