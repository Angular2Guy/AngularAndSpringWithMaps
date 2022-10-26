package ch.xxx.maps.adapter.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import ch.xxx.maps.domain.model.entity.Location;
import ch.xxx.maps.domain.model.entity.LocationRepository;

@Repository
public class LocationRepositoryBean implements LocationRepository {
	private final JpaLocationRepository jpaLocationRepository;
	
	public LocationRepositoryBean(JpaLocationRepository jpaLocalisationRepository) {
		this.jpaLocationRepository = jpaLocalisationRepository;
	}

	@Override
	public void deleteAll(Iterable<Location> locationsToDelete) {
		this.jpaLocationRepository.deleteAll(locationsToDelete);
	}

	@Override
	public List<Location> findAll() {
		return this.jpaLocationRepository.findAll();
	}

	@Override
	public List<Location> findAllByRingIds(List<Long> ids) {
		return this.jpaLocationRepository.findAllByRingIds(ids);
	}

}
