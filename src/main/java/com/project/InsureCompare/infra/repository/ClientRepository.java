package com.project.InsureCompare.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.InsureCompare.domain.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	
	
}