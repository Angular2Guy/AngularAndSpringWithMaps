package ch.xxx.maps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.xxx.maps.model.Location;

public interface LocationRepository extends JpaRepository<Location,Long> {

}
