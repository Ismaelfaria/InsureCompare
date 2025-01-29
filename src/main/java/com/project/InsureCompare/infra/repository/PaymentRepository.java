package com.project.InsureCompare.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.InsureCompare.domain.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {}