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
import org.springframework.stereotype.Controller;

import ch.xxx.maps.domain.exceptions.ResourceNotFoundException;
import ch.xxx.maps.domain.model.dto.CompanySiteDto;
import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.usecase.mapper.EntityDtoMapper;
import ch.xxx.maps.usecase.service.CompanySiteService;
import graphql.language.Field;
import graphql.schema.DataFetchingEnvironment;

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
	public List<CompanySiteDto> getCompanySiteByTitle(@Argument String title, @Argument Long year,
			DataFetchingEnvironment dataFetchingEnvironment) {
//		dataFetchingEnvironment.getDocument().getDefinitions().forEach(myDef -> LOGGER.info(myDef.toString()));
		List<Field> fieldNodes = dataFetchingEnvironment.getDocument().getNamedChildren().getChildOrNull("definitions")
				.getNamedChildren().getChildOrNull("selectionSet").getNamedChildren().getChildOrNull("selections")
				.getNamedChildren().getChildOrNull("selectionSet").getNamedChildren().getChildren("selections");
//		fieldNodes.forEach(myNode -> LOGGER.info(myNode.toString()));
		final boolean[] withPolygons = new boolean[1];
		final boolean[] withRings = new boolean[1];
		boolean withLocations = fieldNodes.stream()
				.filter(myField -> "polygons".equalsIgnoreCase(myField.getName()) && myField.getSelectionSet() != null)
				.peek(myField -> {
					withPolygons[0] = true;
				})
				.flatMap(csField -> csField.getNamedChildren().getChildren("selectionSet").stream()
						.flatMap(myField -> myField.getNamedChildren().getChildren("selections").stream()))
				.filter(myNode -> "rings".equalsIgnoreCase(((Field) myNode).getName())
						&& ((Field) myNode).getSelectionSet() != null)
				.peek(myNode -> {
					withRings[0] = true;
				})
				.flatMap(myNode -> ((Field) myNode).getNamedChildren().getChildren("selectionSet").stream()
						.flatMap(myField -> ((Field) myField).getNamedChildren().getChildren("selections").stream()))
				.anyMatch(myField -> "locations".equalsIgnoreCase(((Field) myField).getName())
						&& ((Field) myField).getSelectionSet() != null);

		List<CompanySiteDto> companySiteDtos = this.companySiteService.findCompanySiteByTitleAndYear(title, year)
				.stream().map(companySite -> this.entityDtoMapper.mapToDto(companySite)).collect(Collectors.toList());
		return companySiteDtos;
	}

	@QueryMapping
	public CompanySiteDto getCompanySiteById(@Argument Long id) {
		CompanySite companySite = this.companySiteService.findCompanySiteById(id)
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

}
