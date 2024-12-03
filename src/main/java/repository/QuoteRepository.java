package repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import domain.Client;
import domain.Quote;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long>{
	
	List<Quote> findByClient(Client customer);
}