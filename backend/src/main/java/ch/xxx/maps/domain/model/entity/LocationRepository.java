package ch.xxx.maps.domain.model.entity;

import java.util.List;

public interface LocationRepository {

	void deleteAll(Iterable<Location> locationsToDelete);

	List<Location> findAll();

	List<Location> findAllByRingIds(List<Long> ids);
}
