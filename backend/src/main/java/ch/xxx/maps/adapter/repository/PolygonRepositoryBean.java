package ch.xxx.maps.adapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.stereotype.Repository;

import ch.xxx.maps.domain.model.entity.Polygon;
import ch.xxx.maps.domain.model.entity.PolygonRepository;
import graphql.schema.DataFetcher;

@Repository
public class PolygonRepositoryBean implements PolygonRepository {
	private final JpaPolygonRepository jpaPolygonRepository;
	
	@Bean("Polygon")
	public DataFetcher<Iterable<Polygon>> createDataFetcher() {
		DataFetcher<Iterable<Polygon>> dataFetcherPg = QuerydslDataFetcher.builder(jpaPolygonRepository).many();
		return dataFetcherPg;
	}
	
	public PolygonRepositoryBean(JpaPolygonRepository jpaPolygonRepository) {
		this.jpaPolygonRepository = jpaPolygonRepository;
	}

	@Override
	public void delete(Polygon polygon) {
		this.jpaPolygonRepository.delete(polygon);
	}

	@Override
	public List<Polygon> findAll() {
		return this.jpaPolygonRepository.findAll();
	}

	@Override
	public void deleteAll(Iterable<Polygon> polygonsToDelete) {
		this.jpaPolygonRepository.deleteAll(polygonsToDelete);
	}

	@Override
	public Optional<Polygon> findById(Long id) {
		return this.jpaPolygonRepository.findById(id);
	}
}
