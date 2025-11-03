package com.example.demo.repository;

import com.example.demo.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findByStatus(Order.OrderStatus status);
}
