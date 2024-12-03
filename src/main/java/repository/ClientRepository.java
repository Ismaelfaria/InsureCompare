package repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	
	@Query("SELECT c FROM Cliente c JOIN c.apolices a JOIN a.seguro s " +
	           "GROUP BY c.id ORDER BY SUM(s.precoBase) DESC")
	    Page<Client> findClientesOrderedByTotalSeguroValue(Pageable pageable);
}