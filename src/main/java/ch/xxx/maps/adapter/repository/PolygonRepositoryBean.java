package ch.xxx.maps.adapter.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ch.xxx.maps.domain.model.entity.Polygon;
import ch.xxx.maps.domain.model.entity.PolygonRepository;

@Repository
public class PolygonRepositoryBean implements PolygonRepository {
	private final JpaPolygonRepository jpaPolygonRepository;
	
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
