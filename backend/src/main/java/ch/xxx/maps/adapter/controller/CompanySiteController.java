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
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import ch.xxx.maps.domain.exceptions.ResourceNotFoundException;
import ch.xxx.maps.domain.model.dto.CompanySiteDto;
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
	public ResponseEntity<List<CompanySiteDto>> getCompanySiteByTitle(@Argument String title,
			@Argument Long year) {
		List<CompanySiteDto> companySiteDtos = this.companySiteService.findCompanySiteByTitleAndYear(title, year)
				.stream().map(companySite -> this.entityDtoMapper.mapToDto(companySite)).collect(Collectors.toList());
		return new ResponseEntity<List<CompanySiteDto>>(companySiteDtos, HttpStatus.OK);
	}

	@QueryMapping
	public ResponseEntity<CompanySiteDto> getCompanySiteById(@Argument Long id) {
		CompanySite companySite = this.companySiteService.findCompanySiteById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("No CompanySite found for id: %d", id)));
		return new ResponseEntity<CompanySiteDto>(this.entityDtoMapper.mapToDto(companySite), HttpStatus.OK);
	}
	
	@MutationMapping
	public ResponseEntity<CompanySiteDto> upsertCompanySite(@Argument CompanySiteDto companySiteDto) {		
		CompanySite companySite = this.companySiteService.findCompanySiteById(companySiteDto.getId()).orElse(new CompanySite());
		companySite = this.companySiteService.upsertCompanySite(this.entityDtoMapper.mapToEntity(companySiteDto, companySite));
		return new ResponseEntity<CompanySiteDto>(this.entityDtoMapper.mapToDto(companySite), HttpStatus.OK);
	}
	
	@MutationMapping
	public ResponseEntity<Boolean> resetDb() {
		return new ResponseEntity<Boolean>(this.companySiteService.resetDb(), HttpStatus.OK);
	}
	
	@MutationMapping
	public ResponseEntity<Boolean> deletePolygon(@Argument Long companySiteId, @Argument Long polygonId) {
		LOGGER.info("companySiteId: {} polygonId: {}", companySiteId, polygonId);
		return new ResponseEntity<Boolean>(this.companySiteService.deletePolygon(companySiteId, polygonId), HttpStatus.OK);
	}
	
	
}
