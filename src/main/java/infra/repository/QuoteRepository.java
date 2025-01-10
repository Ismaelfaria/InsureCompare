package infra.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.entity.Client;
import domain.entity.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long>{}