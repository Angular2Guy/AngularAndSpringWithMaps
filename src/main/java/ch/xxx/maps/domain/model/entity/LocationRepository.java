package ch.xxx.maps.domain.model.entity;

import java.util.Collection;
import java.util.List;

public interface LocationRepository {

	void deleteAll(Collection<Location> locationsToDelete);

	List<Location> findAll();

}
