package ch.xxx.maps.adapter.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.stereotype.Repository;

import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.domain.model.entity.CompanySiteRepository;
import graphql.schema.DataFetcher;

@Repository
public class CompanySiteRepositoryBean implements CompanySiteRepository {
	private final JpaCompanySiteRepository jpaCompanySiteRepository;
	
	public CompanySiteRepositoryBean(JpaCompanySiteRepository jpaCompanySiteRepository) {
		this.jpaCompanySiteRepository = jpaCompanySiteRepository;
	}

	@Bean(name = "CompanySite")
	public DataFetcher<Iterable<CompanySite>> createDataFetcher() {
		DataFetcher<Iterable<CompanySite>> dataFetcherCs = QuerydslDataFetcher.builder(jpaCompanySiteRepository).many();
		return dataFetcherCs;
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
		return this.jpaCompanySiteRepository.save(companySite);
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
