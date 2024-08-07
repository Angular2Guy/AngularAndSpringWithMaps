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
package ch.xxx.maps.usecase.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import ch.xxx.maps.domain.model.dto.CompanySiteDto;
import ch.xxx.maps.domain.model.dto.LocationDto;
import ch.xxx.maps.domain.model.dto.PolygonDto;
import ch.xxx.maps.domain.model.dto.RingDto;
import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.domain.model.entity.Location;
import ch.xxx.maps.domain.model.entity.Polygon;
import ch.xxx.maps.domain.model.entity.Ring;

@Component
public class EntityDtoMapper {

	public CompanySite mapToEntity(CompanySiteDto dto, CompanySite entity) {
		record Polygons(PolygonDto dto, Polygon entity) {};
		entity.setAtDate(dto.getAtDate() == null ? LocalDate.now() : dto.getAtDate());
		entity.setTitle(dto.getTitle());
		entity.setPolygons(dto.getPolygons().stream().flatMap(myPolygonDto -> Stream.of(new Polygons(
				myPolygonDto,
				entity.getPolygons().stream()
						.filter(myPolygon -> myPolygon.getId() != null && entity.getId() != null
								&& myPolygon.getId().equals(myPolygonDto.getId()))
						.findFirst().orElse(new Polygon()))))
				.flatMap(myRecord -> Stream.of(this.mapToEntity(myRecord.dto(), myRecord.entity(), entity)))
				.collect(Collectors.toSet()));
		return entity;
	}

	public Polygon mapToEntity(PolygonDto dto, Polygon entity, CompanySite companySite) {
		record Rings(RingDto dto, Ring entity) {};
		entity.setBorderColor(dto.getBorderColor());
		entity.setLatitude(dto.getLatitude());
		entity.setLongitude(dto.getLongitude());
		entity.setCompanySite(companySite);
		entity.setFillColor(dto.getFillColor());
		entity.setTitle(dto.getTitle());
		entity.setRings(
				dto.getRings().stream()
						.flatMap(myRingDto -> Stream.of(new Rings(myRingDto, entity.getRings().stream()
								.filter(myRing -> myRing.getId() != null && myRingDto.getId() != null
										&& myRing.getId().equals(myRingDto.getId()))
								.findFirst().orElse(new Ring()))))
						.flatMap(myTuple -> Stream.of(this.mapToEntity(myTuple.dto(), myTuple.entity(), entity)))
						.collect(Collectors.toSet()));
		return entity;
	}

	public Ring mapToEntity(RingDto dto, Ring entity, Polygon polygon) {
		record Locations(LocationDto dto, Location entity) { };
		for (int i = 0; i < dto.getLocations().size(); i++) {
			dto.getLocations().get(i).setOrderId(i + 1);
		}
		entity.setPolygon(polygon);
		entity.setPrimaryRing(dto.isPrimaryRing());
		entity.setLocations(dto.getLocations().stream()
				.flatMap(myLocationDto -> Stream.of(new Locations(myLocationDto,
						entity.getLocations().stream()
								.filter(myLocation -> myLocation.getId() != null && myLocationDto.getId() != null
										&& myLocation.getId().equals(myLocationDto.getId()))
								.findFirst().orElse(new Location()))))
				.flatMap(tuple -> Stream.of(this.mapToEntity(tuple.dto(), tuple.entity(), null, entity)))
				.collect(Collectors.toSet()));
		return entity;
	}

	public Location mapToEntity(LocationDto dto, Location entity, Polygon polygonCenter, Ring ring) {
		entity.setLatitude(dto.getLatitude());
		entity.setLongitude(dto.getLongitude());
		entity.setRing(ring);
		entity.setOrderId(polygonCenter == null ? dto.getOrderId() : null);
		return entity;
	}

	public CompanySiteDto mapToDto(CompanySite companySite) {
		CompanySiteDto dto = new CompanySiteDto(companySite.getId(), companySite.getTitle(), companySite.getAtDate(),
				new ArrayList<PolygonDto>());
		return dto;
	}
	
	public PolygonDto mapToDto(Polygon polygon, CompanySiteDto companySiteDto) {
		PolygonDto dto = new PolygonDto(polygon.getId(), polygon.getFillColor(), polygon.getBorderColor(),
				polygon.getTitle(), polygon.getLongitude(), polygon.getLatitude(), new ArrayList<RingDto>());
		companySiteDto.getPolygons().add(dto);
		return dto;
	}
	
	public RingDto mapToDto(Ring ring, PolygonDto polygonDto) {
		var dto = new RingDto(ring.getId(), ring.isPrimaryRing(), new ArrayList<LocationDto>());
		polygonDto.getRings().add(dto);
		return dto;
	}
	
	public LocationDto mapToDto(Location location, RingDto ringDto) {
		var dto = new LocationDto(location.getId(), location.getLongitude(), location.getLatitude(), location.getOrderId());
		ringDto.getLocations().add(dto);
		return dto;
	}
}
