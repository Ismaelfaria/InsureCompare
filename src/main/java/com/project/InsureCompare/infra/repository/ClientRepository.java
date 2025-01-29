package com.project.InsureCompare.infra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.InsureCompare.domain.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	
	//@Query("SELECT c FROM Client c JOIN c.InsurancePolicy a JOIN a.Insurance s " +
	   //        "GROUP BY c.id ORDER BY SUM(s.basePrice) DESC")
	@Query("""
		    SELECT c FROM Client c 
		    LEFT JOIN c.insurancePolicy p 
		    LEFT JOIN p.insurance i 
		    GROUP BY c.id 
		    ORDER BY COALESCE(SUM(i.basePrice), 0) DESC
		""")
	    Page<Client> findClientesOrderedByTotalSeguroValue(Pageable pageable);
}