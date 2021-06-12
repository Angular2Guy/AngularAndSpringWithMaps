package ch.xxx.maps.domain.model.entity;

import java.util.Collection;
import java.util.List;

public interface RingRepository {

	void deleteAll(Collection<Ring> ringsToDelete);

	List<Ring> findAll();

}
