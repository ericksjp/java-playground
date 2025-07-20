package com.web.course.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.course.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
