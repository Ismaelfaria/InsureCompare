package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import domain.Insurance;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
	
	@Query("SELECT i FROM Insurance i ORDER BY i.precoBase ASC")
    List<Insurance> findAllSortedByPrecoBaseAsc();
}