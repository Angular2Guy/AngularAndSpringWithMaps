package ch.xxx.maps.domain.model.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CompanySiteRepository {

	Collection<CompanySite> findByTitleFromTo(String lowerCase, LocalDate beginOfYear, LocalDate endOfYear);  
	Optional<CompanySite> findById(Long id);

	CompanySite save(CompanySite companySite);

	List<CompanySite> findAll();

	void deleteAll(Iterable<CompanySite> companySitesToDelete);

}
