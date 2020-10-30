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

import java.util.List;
import java.util.stream.Collectors;

import ch.xxx.maps.dto.CompanySiteDto;
import ch.xxx.maps.dto.LocationDto;
import ch.xxx.maps.dto.PolygonDto;
import ch.xxx.maps.dto.RingDto;
import ch.xxx.maps.model.CompanySite;
import ch.xxx.maps.model.Location;
import ch.xxx.maps.model.Polygon;
import ch.xxx.maps.model.Ring;

public class EntityToDtoMapper {

	public static CompanySiteDto mapToDto(CompanySite companySite) {
		List<PolygonDto> myPolygons = companySite.getPolygons().stream()
				.map(polygon -> EntityToDtoMapper.mapToDto(polygon)).collect(Collectors.toList());
		CompanySiteDto dto = new CompanySiteDto(companySite.getId(), companySite.getTitle(), companySite.getAtDate(),
				myPolygons);
		return dto;
	}

	public static PolygonDto mapToDto(Polygon polygon) {
		List<RingDto> myRings = polygon.getRings().stream().map(ring -> EntityToDtoMapper.mapToDto(ring))
				.collect(Collectors.toList());
		LocationDto centerLocationDto = polygon.getCenterLocation() == null ? null
				: EntityToDtoMapper.mapToDto(polygon.getCenterLocation());
		PolygonDto dto = new PolygonDto(polygon.getId(), polygon.getFillColor(), polygon.getBorderColor(),
				polygon.getTitle(), centerLocationDto, myRings);
		return dto;
	}

	public static RingDto mapToDto(Ring ring) {
		List<LocationDto> myLocations = ring.getLocations().stream()
				.map(location -> EntityToDtoMapper.mapToDto(location)).collect(Collectors.toList());
		RingDto dto = new RingDto(ring.getId(), ring.isPrimary(), myLocations);
		return dto;
	}

	public static LocationDto mapToDto(Location location) {
		LocationDto dto = new LocationDto(location.getId(), location.getLongitude(), location.getLatitude());
		return dto;
	}
}
