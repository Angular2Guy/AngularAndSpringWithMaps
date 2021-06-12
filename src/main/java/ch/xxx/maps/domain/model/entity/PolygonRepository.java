package ch.xxx.maps.domain.model.entity;

import java.util.List;
import java.util.Optional;

public interface PolygonRepository {

	void delete(Polygon polygon);

	List<Polygon> findAll();

	void deleteAll(Iterable<Polygon> polygonsToDelete);
	
	Optional<Polygon> findById(Long id);
}
