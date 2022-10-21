package ch.xxx.maps.domain.model.entity;

import java.util.List;

public interface RingRepository {

	void deleteAll(Iterable<Ring> ringsToDelete);

	List<Ring> findAll();

	List<Ring> findAllByPolygonIds(List<Long> ids);
}
