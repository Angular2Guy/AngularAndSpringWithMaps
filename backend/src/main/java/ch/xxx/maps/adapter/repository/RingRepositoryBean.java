package ch.xxx.maps.adapter.repository;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.stereotype.Repository;

import ch.xxx.maps.domain.model.entity.Ring;
import ch.xxx.maps.domain.model.entity.RingRepository;
import graphql.schema.DataFetcher;

@Repository
public class RingRepositoryBean implements RingRepository {
	private final JpaRingRepository jpaRingRepository;

	public RingRepositoryBean(JpaRingRepository jpaRingRepository) {
		this.jpaRingRepository = jpaRingRepository;
	}

	@Bean("Ring")
	public DataFetcher<Iterable<Ring>> createDataFetcher() {
		DataFetcher<Iterable<Ring>> dataFetcherRi = QuerydslDataFetcher.builder(jpaRingRepository).many();
		return dataFetcherRi;
	}

	@Override
	public void deleteAll(Iterable<Ring> ringsToDelete) {
		this.jpaRingRepository.deleteAll(ringsToDelete);
	}

	@Override
	public List<Ring> findAll() {
		return this.jpaRingRepository.findAll();
	}
}
