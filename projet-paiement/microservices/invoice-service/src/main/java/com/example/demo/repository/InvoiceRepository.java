package com.example.demo.repository;

import com.example.demo.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	Optional<Invoice> findByOrderId(Long orderId);
	List<Invoice> findByUserId(Long userId);
	Boolean existsByOrderId(Long orderId);
	List<Invoice> findByStatus(Invoice.InvoiceStatus status);
}