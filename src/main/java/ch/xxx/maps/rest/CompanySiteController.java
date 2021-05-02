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
package ch.xxx.maps.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.xxx.maps.dto.CompanySiteDto;
import ch.xxx.maps.exceptions.ResourceNotFoundException;
import ch.xxx.maps.model.CompanySite;
import ch.xxx.maps.service.CompanySiteService;
import ch.xxx.maps.service.EntityDtoMapper;

@RestController
@RequestMapping("rest/companySite")
public class CompanySiteController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanySite.class);
	private final CompanySiteService companySiteService;

	public CompanySiteController(CompanySiteService companySiteService) {
		this.companySiteService = companySiteService;
	}

	@RequestMapping(value = "/title/{title}/year/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CompanySiteDto>> getCompanySiteByTitle(@PathVariable("title") String title,
			@PathVariable("year") Long year) {
		List<CompanySiteDto> companySiteDtos = this.companySiteService.findCompanySiteByTitleAndYear(title, year)
				.stream().map(companySite -> EntityDtoMapper.mapToDto(companySite)).collect(Collectors.toList());
		return new ResponseEntity<List<CompanySiteDto>>(companySiteDtos, HttpStatus.OK);
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompanySiteDto> getCompanySiteById(@PathVariable("id") Long id) {
		CompanySite companySite = this.companySiteService.findCompanySiteById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("No CompanySite found for id: %d", id)));
		return new ResponseEntity<CompanySiteDto>(EntityDtoMapper.mapToDto(companySite), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CompanySiteDto> upsertCompanySite(@RequestBody CompanySiteDto companySiteDto) {		
		CompanySite companySite = this.companySiteService.findCompanySiteById(companySiteDto.getId()).orElse(new CompanySite());
		companySite = this.companySiteService.upsertCompanySite(EntityDtoMapper.mapToEntity(companySiteDto, companySite));
		return new ResponseEntity<CompanySiteDto>(EntityDtoMapper.mapToDto(companySite), HttpStatus.OK);
	}
	
	@RequestMapping(value="/reset",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> resetDb() {
		return new ResponseEntity<Boolean>(this.companySiteService.resetDb(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/id/{companySiteId}/polygon/id/{polygonId}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deletePolygon(@PathVariable Long companySiteId, @PathVariable Long polygonId) {
		LOGGER.info("companySiteId: {} polygonId: {}", companySiteId, polygonId);
		return new ResponseEntity<Boolean>(this.companySiteService.deletePolygon(companySiteId, polygonId), HttpStatus.OK);
	}
}
