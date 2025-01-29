package com.project.InsureCompare.infra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.domain.entity.InsurancePolicy;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {
	@Query("""
	        SELECT ip FROM InsurancePolicy ip
	        WHERE (:clientId IS NULL OR ip.client.id = :clientId)
	        AND ip.status = :status
	    """)
	List<InsurancePolicy> findByCustomerAndStatus(Optional<Client> client, String status);
}