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
package ch.xxx.maps.usecase.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.xxx.maps.domain.model.entity.BaseEntity;
import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.domain.model.entity.CompanySiteRepository;
import ch.xxx.maps.domain.model.entity.Location;
import ch.xxx.maps.domain.model.entity.LocationRepository;
import ch.xxx.maps.domain.model.entity.Polygon;
import ch.xxx.maps.domain.model.entity.PolygonRepository;
import ch.xxx.maps.domain.model.entity.Ring;
import ch.xxx.maps.domain.model.entity.RingRepository;
import ch.xxx.maps.domain.utils.StreamUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Transactional
@Service
public class CompanySiteService {
	private final CompanySiteRepository companySiteRepository;
	private final PolygonRepository polygonRepository;
	private final RingRepository ringRepository;
	private final LocationRepository locationRepository;
	private final DataFetcher<Iterable<CompanySite>> dataFetcherCs;

	public CompanySiteService(@Qualifier("CompanySite") DataFetcher<Iterable<CompanySite>> dataFetcherCs,
			CompanySiteRepository companySiteRepository, PolygonRepository polygonRepository,
			RingRepository ringRepository, LocationRepository locationRepository) {
		this.companySiteRepository = companySiteRepository;
		this.polygonRepository = polygonRepository;
		this.ringRepository = ringRepository;
		this.locationRepository = locationRepository;
		this.dataFetcherCs = dataFetcherCs;
	}

	public Collection<CompanySite> findCompanySiteByTitleAndYear(String title, Long year) {
		if (title == null || title.length() < 2) {
			return List.of();
		}
		LocalDate beginOfYear = LocalDate.of(year.intValue(), 1, 1);
		LocalDate endOfYear = LocalDate.of(year.intValue(), 12, 31);
		title = title.trim().toLowerCase();
		return this.companySiteRepository.findByTitleFromTo(title, beginOfYear, endOfYear);
	}

	public Optional<CompanySite> findCompanySiteById(Long id) {
		return Optional.ofNullable(id).flatMap(myId -> this.companySiteRepository.findById(myId));
	}

	public Collection<CompanySite> findCompanySiteByDaFetchEnv(DataFetchingEnvironment dataFetchingEnvironment) {
		Collection<CompanySite> result = List.of();
		try {
			Iterable<CompanySite> iterable = this.dataFetcherCs.get(dataFetchingEnvironment);
			result = StreamUtils.toStream(iterable).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public CompanySite upsertCompanySite(CompanySite companySite) {
		return this.companySiteRepository.save(companySite);
	}

	public boolean deletePolygon(Long companySiteId, Long polygonId) {
		Optional<CompanySite> companySiteOpt = this.companySiteRepository.findById(companySiteId);
		if (companySiteOpt.isEmpty()) {
			return false;
		}
		Optional<Polygon> polygonOpt = companySiteOpt.get().getPolygons().stream()
				.filter(myPolygon -> myPolygon.getId() >= 1000L && myPolygon.getId().equals(polygonId)).findFirst();
		if (polygonOpt.isEmpty()) {
			return false;
		}
		companySiteOpt.get().getPolygons().remove(polygonOpt.get());
		polygonOpt.get().setCompanySite(null);
		Set<Ring> ringsToDelete = polygonOpt.get().getRings();
		polygonOpt.get().setRings(null);
		Set<Location> locationsToDelete = new HashSet<>();
		ringsToDelete.forEach(myRing -> {
			myRing.setPolygon(null);
			locationsToDelete.addAll(myRing.getLocations());
			myRing.setLocations(null);
		});
		locationsToDelete.forEach(myLocation -> {
			myLocation.setRing(null);
		});
		this.locationRepository.deleteAll(locationsToDelete);
		this.ringRepository.deleteAll(ringsToDelete);
		this.polygonRepository.delete(polygonOpt.get());
		return true;
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
		List<Location> allLocations = this.locationRepository.findAll();
		List<Location> locationsToDelete = allLocations.stream().filter(location -> location.getId() >= 1000)
				.collect(Collectors.toList());
		locationsToDelete.forEach(myLocaton -> myLocaton.setRing(null));
		allRings.forEach(myRing -> myRing.getLocations().removeAll(locationsToDelete));
		this.locationRepository.deleteAll(locationsToDelete);
		this.ringRepository.deleteAll(ringsToDelete);
		this.polygonRepository.deleteAll(polygonsToDelete);
		this.companySiteRepository.deleteAll(companySitesToDelete);
		return true;
	}

	public Map<CompanySite, List<Polygon>> fetchPolygons(List<CompanySite> companySites) {
		List<Polygon> polygons = this.polygonRepository
				.findAllByCompanySiteIds(companySites.stream().map(cs -> cs.getId()).collect(Collectors.toList()));
		return companySites.stream().map(CompanySite::getId)
				.map(myCsId -> Map.entry(findEntity(companySites, myCsId), findPolygons(polygons, myCsId)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private List<Polygon> findPolygons(List<Polygon> polygons, Long myCsId) {
		return polygons.stream().filter(myPolygon -> myPolygon.getCompanySite().getId().equals(myCsId)).toList();
	}

	private <T extends BaseEntity> T findEntity(List<T> companySites, Long myCsId) {
		return companySites.stream().filter(myCs -> myCs.getId().equals(myCsId)).findFirst().orElseThrow();
	}

	public Map<Polygon, List<Ring>> fetchRings(List<Polygon> polygons) {
		List<Ring> rings = this.ringRepository
				.findAllByPolygonIds(polygons.stream().map(Polygon::getId).collect(Collectors.toList()));
		return polygons.stream().map(Polygon::getId)
				.map(myPgId -> Map.entry(this.findEntity(polygons, myPgId), findRings(rings, myPgId)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private List<Ring> findRings(List<Ring> rings, Long myPgId) {
		return rings.stream().filter(myRing -> myRing.getPolygon().getId().equals(myPgId)).collect(Collectors.toList());
	}

	public Map<Ring, List<Location>> fetchLocations(List<Ring> rings) {
		List<Location> locations = this.locationRepository
				.findAllByRingIds(rings.stream().map(Ring::getId).collect(Collectors.toList()));
		return locations.stream().map(Location::getId)
				.map(myRiId -> Map.entry(this.findEntity(rings, myRiId),
						findLocations(locations, myRiId)))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private List<Location> findLocations(List<Location> locations, Long myRiId) {
		return locations.stream().filter(myLocation -> myLocation.getRing().getId().equals(myRiId))
				.collect(Collectors.toList());
	}
}
