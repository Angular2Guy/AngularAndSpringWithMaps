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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.xxx.maps.model.CompanySite;
import ch.xxx.maps.model.Location;
import ch.xxx.maps.repository.CompanySiteRepository;

@Transactional
@Service
public class CompanySiteService {
	private final CompanySiteRepository companySiteRepository;

	public CompanySiteService(CompanySiteRepository companySiteRepository) {
		this.companySiteRepository = companySiteRepository;
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
				.peek(companySite -> companySite.getPolygons()
						.forEach(polygon -> polygon.getRings()
								.forEach(ring -> ring.setLocations(new LinkedHashSet<Location>(ring.getLocations()
										.stream().sorted((Location l1, Location l2) -> l1.getId().compareTo(l2.getId()))
										.collect(Collectors.toList()))))))
				.collect(Collectors.toList());
	}

	public Optional<CompanySite> findCompanySiteById(Long id) {
		return this.companySiteRepository.findById(id);
	}
}
