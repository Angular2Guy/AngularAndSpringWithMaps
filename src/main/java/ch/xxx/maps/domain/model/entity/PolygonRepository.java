package ch.xxx.maps.domain.model.entity;

import java.util.Collection;
import java.util.List;

public interface PolygonRepository {

	void delete(Polygon polygon);

	List<Polygon> findAll();

	void deleteAll(Collection<Polygon> polygonsToDelete);
}
