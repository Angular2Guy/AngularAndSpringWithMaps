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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public Collection<CompanySite> findCompanySiteByTitleAndYear(String title, Long year, boolean withPolygons,
			boolean withRings, boolean withLocations) {
		if (title == null || title.length() < 2) {
			return List.of();
		}
		LocalDate beginOfYear = LocalDate.of(year.intValue(), 1, 1);
		LocalDate endOfYear = LocalDate.of(year.intValue(), 12, 31);
		title = title.trim().toLowerCase();
		return withPolygons || withRings || withLocations
				? this.companySiteRepository.findByTitleFromToWithChildren(title, beginOfYear, endOfYear)
				: this.companySiteRepository.findByTitleFromTo(title, beginOfYear, endOfYear);
	}

	public Optional<CompanySite> findCompanySiteById(Long id, boolean withPolygons, boolean withRings,
			boolean withLocations) {
		return Optional.ofNullable(id)
				.flatMap(myId -> withPolygons || withRings || withLocations
						? this.companySiteRepository.findByIdWithChildren(id)
						: this.companySiteRepository.findById(myId));
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
}
