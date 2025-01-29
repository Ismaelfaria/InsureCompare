package com.project.InsureCompare.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.InsureCompare.domain.entity.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long>{}