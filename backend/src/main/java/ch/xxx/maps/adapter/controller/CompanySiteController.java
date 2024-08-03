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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import ch.xxx.maps.domain.exceptions.ResourceNotFoundException;
import ch.xxx.maps.domain.model.dto.CompanySiteDto;
import ch.xxx.maps.domain.model.dto.LocationDto;
import ch.xxx.maps.domain.model.dto.PolygonDto;
import ch.xxx.maps.domain.model.dto.RingDto;
import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.usecase.mapper.EntityDtoMapper;
import ch.xxx.maps.usecase.service.CompanySiteService;

@Controller
public class CompanySiteController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanySite.class);
	private final CompanySiteService companySiteService;
	private final EntityDtoMapper entityDtoMapper;

	public CompanySiteController(CompanySiteService companySiteService, EntityDtoMapper entityDtoMapper) {
		this.companySiteService = companySiteService;
		this.entityDtoMapper = entityDtoMapper;
	}

	@QueryMapping
	public List<CompanySiteDto> getCompanySiteByTitle(@Argument("title") String title, @Argument("year") Long year) {
		List<CompanySiteDto> companySiteDtos = this.companySiteService
				.findCompanySiteByTitleAndYear(title, year)
				.stream().map(this.entityDtoMapper::mapToDto).collect(Collectors.toList());
		return companySiteDtos;
	}

	@QueryMapping
	public CompanySiteDto getCompanySiteById(@Argument("id") Long id) {
		return this.companySiteService
				.findCompanySiteByIdDetached(id)
				.stream().map(this.entityDtoMapper::mapToDto).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException(String.format("No CompanySite found for id: %d", id)));
	}

	@BatchMapping(typeName = "CompanySiteOut")
	public List<List<PolygonDto>> polygons(List<CompanySiteDto> companySiteDtos) {
		return this.companySiteService.fetchPolygonDtos(companySiteDtos).entrySet().stream().map(value -> value
				.getValue().stream().map(myValue -> this.entityDtoMapper.mapToDto(myValue, value.getKey())).toList())
				.toList();
	}

	@BatchMapping(typeName = "PolygonOut")
	public List<List<RingDto>> rings(List<PolygonDto> polygonDtos) {
		return this.companySiteService.fetchRingDtos(polygonDtos).entrySet().stream().map(value -> value.getValue()
				.stream().map(myValue -> this.entityDtoMapper.mapToDto(myValue, value.getKey())).toList()).toList();
	}

	@BatchMapping(typeName = "RingOut")
	public List<List<LocationDto>> locations(List<RingDto> ringDtos) {
		return this.companySiteService.fetchLocationDtos(ringDtos).entrySet().stream().map(value -> value.getValue()
				.stream().map(myValue -> this.entityDtoMapper.mapToDto(myValue, value.getKey())).toList()).toList();
	}

	@MutationMapping
	public CompanySiteDto upsertCompanySite(@Argument(value = "companySite") CompanySiteDto companySiteDto) {
		CompanySite companySite = this.companySiteService.findCompanySiteById(companySiteDto.getId())
				.orElse(new CompanySite());
		return this.entityDtoMapper.mapToDto(this.companySiteService
				.upsertCompanySite(this.entityDtoMapper.mapToEntity(companySiteDto, companySite)));
	}

	@MutationMapping
	public Boolean resetDb() {
		return this.companySiteService.resetDb();
	}

	@MutationMapping
	public Boolean deletePolygon(@Argument("companySiteId") Long companySiteId, @Argument("polygonId") Long polygonId) {
		LOGGER.info("companySiteId: {} polygonId: {}", companySiteId, polygonId);
		return this.companySiteService.deletePolygon(companySiteId, polygonId);
	}
}
