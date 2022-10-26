package ch.xxx.maps.adapter.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import ch.xxx.maps.domain.model.entity.Ring;
import ch.xxx.maps.domain.model.entity.RingRepository;

@Repository
public class RingRepositoryBean implements RingRepository {
	private final JpaRingRepository jpaRingRepository;

	public RingRepositoryBean(JpaRingRepository jpaRingRepository) {
		this.jpaRingRepository = jpaRingRepository;
	}

	@Override
	public void deleteAll(Iterable<Ring> ringsToDelete) {
		this.jpaRingRepository.deleteAll(ringsToDelete);
	}

	@Override
	public List<Ring> findAll() {
		return this.jpaRingRepository.findAll();
	}

	@Override
	public List<Ring> findAllByPolygonIds(List<Long> ids) {
		return this.jpaRingRepository.findAllByPolygonIds(ids);
	}
}
