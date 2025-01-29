package com.project.InsureCompare.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.InsureCompare.domain.entity.Insurance;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
	
	@Query("SELECT i FROM Insurance i ORDER BY i.basePrice ASC")
    List<Insurance> findAllSortedByPrecoBaseAsc();
}