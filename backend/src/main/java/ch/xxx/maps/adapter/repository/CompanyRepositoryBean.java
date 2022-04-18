package ch.xxx.maps.adapter.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.domain.model.entity.CompanySiteRepository;

@Repository
public class CompanyRepositoryBean implements CompanySiteRepository {
	private final JpaCompanySiteRepository jpaCompanySiteRepository;
	
	public CompanyRepositoryBean(JpaCompanySiteRepository jpaCompanySiteRepository) {
		this.jpaCompanySiteRepository = jpaCompanySiteRepository;
	}

	@Override
	public Collection<CompanySite> findByTitleFromTo(String lowerCase, LocalDate beginOfYear, LocalDate endOfYear) {
		return this.jpaCompanySiteRepository.findByTitleFromTo(lowerCase, beginOfYear, endOfYear);
	}

	@Override
	public Optional<CompanySite> findById(Long id) {
		return this.jpaCompanySiteRepository.findById(id);
	}

	@Override
	public CompanySite save(CompanySite companySite) {
		return this.jpaCompanySiteRepository. save(companySite);
	}

	@Override
	public List<CompanySite> findAll() {
		return this.jpaCompanySiteRepository.findAll();
	}

	@Override
	public void deleteAll(Iterable<CompanySite> companySitesToDelete) {
		this.jpaCompanySiteRepository.deleteAll(companySitesToDelete);
	}
}
