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
package ch.xxx.maps.adapter.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import ch.xxx.maps.domain.exceptions.ResourceNotFoundException;
import ch.xxx.maps.domain.model.dto.CompanySiteDto;
import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.usecase.mapper.EntityDtoMapper;
import ch.xxx.maps.usecase.service.CompanySiteService;
import graphql.schema.DataFetchingEnvironment;

@Controller
public class CompanySiteController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanySite.class);
	private final CompanySiteService companySiteService;
	private final EntityDtoMapper entityDtoMapper;
	private record Selections(boolean withPolygons, boolean withRings, boolean withLocations) {	}

	public CompanySiteController(CompanySiteService companySiteService, EntityDtoMapper entityDtoMapper) {
		this.companySiteService = companySiteService;
		this.entityDtoMapper = entityDtoMapper;
	}

	@QueryMapping
	public List<CompanySiteDto> getCompanySiteByTitle(@Argument String title, @Argument Long year,
			DataFetchingEnvironment dataFetchingEnvironment) {
		Selections selections = createSelections(dataFetchingEnvironment);
		List<CompanySiteDto> companySiteDtos = this.companySiteService.findCompanySiteByTitleAndYear(title, year, selections.withPolygons(), selections.withRings(), selections.withLocations())
				.stream().map(companySite -> this.entityDtoMapper.mapToDto(companySite))
				.collect(Collectors.toList());
		return companySiteDtos;
	}

	private Selections createSelections(DataFetchingEnvironment dataFetchingEnvironment) {
		boolean addPolygons = dataFetchingEnvironment.getSelectionSet().contains("polygons");
		boolean addRings = dataFetchingEnvironment.getSelectionSet().getFields().stream()
				.anyMatch(sf -> "rings".equalsIgnoreCase(sf.getName()));
		boolean addLocations = dataFetchingEnvironment.getSelectionSet().getFields().stream()
				.filter(sf -> "rings".equalsIgnoreCase(sf.getName())).flatMap(sf -> Stream.of(sf.getSelectionSet()))
				.anyMatch(sf -> sf.contains("locations"));
		Selections selections = new Selections(addPolygons, addRings, addLocations);
		return selections;
	}

	@QueryMapping
	public CompanySiteDto getCompanySiteById(@Argument Long id, DataFetchingEnvironment dataFetchingEnvironment) {
		Selections selections = createSelections(dataFetchingEnvironment);
		CompanySite companySite = this.companySiteService.findCompanySiteByIdDetached(id, selections.withPolygons(), selections.withRings(), selections.withLocations())
				.orElseThrow(() -> new ResourceNotFoundException(String.format("No CompanySite found for id: %d", id)));
		return this.entityDtoMapper.mapToDto(companySite);
	}

	@MutationMapping
	public CompanySiteDto upsertCompanySite(@Argument(value = "companySite") CompanySiteDto companySiteDto) {
		CompanySite companySite = this.companySiteService.findCompanySiteById(companySiteDto.getId())
				.orElse(new CompanySite());
		companySite = this.companySiteService
				.upsertCompanySite(this.entityDtoMapper.mapToEntity(companySiteDto, companySite));
		return this.entityDtoMapper.mapToDto(companySite);
	}

	@MutationMapping
	public Boolean resetDb() {
		return this.companySiteService.resetDb();
	}

	@MutationMapping
	public Boolean deletePolygon(@Argument Long companySiteId, @Argument Long polygonId) {
		LOGGER.info("companySiteId: {} polygonId: {}", companySiteId, polygonId);
		return this.companySiteService.deletePolygon(companySiteId, polygonId);
	}

//	@BatchMapping(field = "polygons", typeName = "CompanySiteOut")
//	public Mono<Map<CompanySite, List<Polygon>>> fetchPolygons(List<CompanySiteDto> companySites) {
//		LOGGER.info("Fetching polygons");
//		Map<CompanySite, List<Polygon>> result = companySiteService.fetchPolygons(companySites.stream()
//				.map(myDto -> {
//					CompanySite entity = this.entityDtoMapper.mapToEntity(myDto, new CompanySite());
//					entity.setId(myDto.getId());
//					return entity;
//				}).toList());
//		LOGGER.info("Polygons Done.");
//		return Mono.just(result);
//	}
//
//	@BatchMapping(field = "rings", typeName = "PolygonOut")
//	public Map<Polygon, List<Ring>> fetchRings(List<Polygon> polygons) {
//		LOGGER.info("Fetching rings");
//		Map<Polygon, List<Ring>> result = companySiteService.fetchRings(polygons);
//		return result;
//	}
//
//	@BatchMapping(field = "locations", typeName= "RingOut")
//	public Map<Ring, List<Location>> fetchLocations(List<Ring> polygons) {
//		LOGGER.info("Fetching rings");
//		Map<Ring, List<Location>> result = companySiteService.fetchLocations(polygons);
//		return result;
//	}
}
