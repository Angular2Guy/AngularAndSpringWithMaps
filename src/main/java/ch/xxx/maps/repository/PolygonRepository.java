package ch.xxx.maps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.xxx.maps.model.Polygon;

public interface PolygonRepository extends JpaRepository<Polygon, Long>{

}
