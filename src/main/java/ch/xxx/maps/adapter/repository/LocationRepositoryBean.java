package ch.xxx.maps.adapter.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import ch.xxx.maps.domain.model.entity.Location;
import ch.xxx.maps.domain.model.entity.LocationRepository;

@Repository
public class LocationRepositoryBean implements LocationRepository {
	private final JpaLocationRepository jpaLocalisationRepository;
	
	public LocationRepositoryBean(JpaLocationRepository jpaLocalisationRepository) {
		this.jpaLocalisationRepository = jpaLocalisationRepository;
	}

	@Override
	public void deleteAll(Iterable<Location> locationsToDelete) {
		this.jpaLocalisationRepository.deleteAll(locationsToDelete);
	}

	@Override
	public List<Location> findAll() {
		return this.jpaLocalisationRepository.findAll();
	}

}
