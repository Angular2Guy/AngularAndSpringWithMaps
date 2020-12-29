/**
 *    Copyright 2019 Sven Loesekann
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package ch.xxx.maps.service;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.xxx.maps.model.CompanySite;
import ch.xxx.maps.model.Location;
import ch.xxx.maps.model.Polygon;
import ch.xxx.maps.model.Ring;
import ch.xxx.maps.repository.CompanySiteRepository;
import ch.xxx.maps.repository.LocationRepository;
import ch.xxx.maps.repository.PolygonRepository;
import ch.xxx.maps.repository.RingRepository;

@Transactional
@Service
public class CompanySiteService {
	private final CompanySiteRepository companySiteRepository;
	private final PolygonRepository polygonRepository;
	private final RingRepository ringRepository;
	private final LocationRepository locationRepository;

	public CompanySiteService(CompanySiteRepository companySiteRepository, PolygonRepository polygonRepository,
			RingRepository ringRepository, LocationRepository locationRepository) {
		this.companySiteRepository = companySiteRepository;
		this.polygonRepository = polygonRepository;
		this.ringRepository = ringRepository;
		this.locationRepository = locationRepository;
	}

	public List<CompanySite> findCompanySiteByTitleAndYear(String title, Long year) {
		if (title == null || title.length() < 2) {
			return List.of();
		}
		LocalDate beginOfYear = LocalDate.of(year.intValue(), 1, 1);
		LocalDate endOfYear = LocalDate.of(year.intValue(), 12, 31);
		return this.companySiteRepository.findByTitle(title.toLowerCase()).stream()
				.filter(companySite -> beginOfYear.isBefore(companySite.getAtDate())
						&& endOfYear.isAfter(companySite.getAtDate()))
				.peek(companySite -> this.orderCompanySite(companySite)).collect(Collectors.toList());
	}

	private CompanySite orderCompanySite(CompanySite companySite) {
		companySite.getPolygons()
				.forEach(polygon -> polygon.getRings()
						.forEach(ring -> ring.setLocations(new LinkedHashSet<Location>(ring.getLocations().stream()
								.sorted((Location l1, Location l2) -> l1.getOrderId().compareTo(l2.getOrderId()))
								.collect(Collectors.toList())))));
		return companySite;
	}

	public Optional<CompanySite> findCompanySiteById(Long id) {
		return id == null ? Optional.empty() : this.companySiteRepository.findById(id).map(myCompanySite -> this.orderCompanySite(myCompanySite));
	}

	public CompanySite upsertCompanySite(CompanySite companySite) {
		return this.orderCompanySite(this.companySiteRepository.save(companySite));
	}

	public boolean resetDb() {
		List<CompanySite> allCompanySites = this.companySiteRepository.findAll();
		List<CompanySite> companySitesToDelete = allCompanySites.stream()
				.filter(companySite -> companySite.getId() >= 1000).collect(Collectors.toList());
		List<Polygon> allPolygons = this.polygonRepository.findAll();
		List<Polygon> polygonsToDelete = allPolygons.stream().filter(polygon -> polygon.getId() >= 1000)
				.collect(Collectors.toList());
		allCompanySites.forEach(myCompanySite -> myCompanySite.getPolygons().removeAll(polygonsToDelete));
		List<Ring> allRings = this.ringRepository.findAll();
		List<Ring> ringsToDelete = allRings.stream().filter(ring -> ring.getId() >= 1000).collect(Collectors.toList());
		allPolygons.forEach(myPolygon -> myPolygon.getRings().removeAll(ringsToDelete));
		polygonsToDelete.forEach(myPolygon -> myPolygon.setCenterLocation(null));
		List<Location> allLocations = this.locationRepository.findAll();
		List<Location> locationsToDelete = allLocations.stream().filter(location -> location.getId() >= 1000)
				.collect(Collectors.toList());
		locationsToDelete.forEach(myLocaton -> {
			myLocaton.setPolygonCenter(null);
			myLocaton.setRing(null);
		});
		allRings.forEach(myRing -> {
			myRing.getLocations().removeAll(locationsToDelete);
		});
		this.locationRepository.deleteAll(locationsToDelete);
		this.ringRepository.deleteAll(ringsToDelete);
		this.polygonRepository.deleteAll(polygonsToDelete);
		this.companySiteRepository.deleteAll(companySitesToDelete);
		return true;
	}
}
