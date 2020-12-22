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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.xxx.maps.dto.CompanySiteDto;
import ch.xxx.maps.dto.LocationDto;
import ch.xxx.maps.dto.PolygonDto;
import ch.xxx.maps.dto.RingDto;
import ch.xxx.maps.dto.Tuple;
import ch.xxx.maps.model.CompanySite;
import ch.xxx.maps.model.Location;
import ch.xxx.maps.model.Polygon;
import ch.xxx.maps.model.Ring;

public class EntityDtoMapper {

	public static CompanySite mapToEntity(CompanySiteDto dto, CompanySite entity) {
		entity.setAtDate(dto.getAtDate() == null ? LocalDate.now() : dto.getAtDate());
		entity.setTitle(dto.getTitle());
		entity.setPolygons(dto.getPolygons().stream().flatMap(myPolygonDto -> Stream.of(new Tuple<PolygonDto, Polygon>(
				myPolygonDto,
				entity.getPolygons().stream()
						.filter(myPolygon -> myPolygon.getId() != null && entity.getId() != null
								&& myPolygon.getId().equals(myPolygonDto.getId()))
						.findFirst().orElse(new Polygon()))))
				.flatMap(tuple -> Stream.of(EntityDtoMapper.mapToEntity(tuple.getA(), tuple.getB(), entity)))
				.collect(Collectors.toSet()));
		return entity;
	}

	public static Polygon mapToEntity(PolygonDto dto, Polygon entity, CompanySite companySite) {
		entity.setBorderColor(dto.getBorderColor());
		entity.setCenterLocation(
				EntityDtoMapper.mapToEntity(dto.getCenterLocation(), entity.getCenterLocation(), entity, null));
		entity.setCompanySite(companySite);
		entity.setFillColor(dto.getFillColor());
		entity.setTitle(dto.getTitle());
		entity.setRings(
				dto.getRings().stream()
						.flatMap(myRingDto -> Stream.of(new Tuple<RingDto, Ring>(myRingDto, entity.getRings().stream()
								.filter(myRing -> myRing.getId() != null && myRingDto.getId() != null
										&& myRing.getId().equals(myRingDto.getId()))
								.findFirst().orElse(new Ring()))))
						.flatMap(myTuple -> Stream
								.of(EntityDtoMapper.mapToEntity(myTuple.getA(), myTuple.getB(), entity)))
						.collect(Collectors.toSet()));
		return entity;
	}

	public static Ring mapToEntity(RingDto dto, Ring entity, Polygon polygon) {
		entity.setPolygon(polygon);
		entity.setPrimaryRing(dto.isPrimary());
		entity.setLocations(dto.getLocations().stream()
				.flatMap(myLocationDto -> Stream.of(new Tuple<LocationDto, Location>(myLocationDto,
						entity.getLocations().stream()
								.filter(myLocation -> myLocation.getId() != null && myLocationDto.getId() != null
										&& myLocation.getId().equals(myLocationDto.getId()))
								.findFirst().orElse(new Location()))))
				.flatMap(tuple -> Stream.of(EntityDtoMapper.mapToEntity(tuple.getA(), tuple.getB(), null, entity)))
				.collect(Collectors.toSet()));
		return entity;
	}

	public static Location mapToEntity(LocationDto dto, Location entity, Polygon polygonCenter, Ring ring) {
		entity.setLatitude(dto.getLatitude());
		entity.setLongitude(dto.getLongitude());
		entity.setPolygonCenter(polygonCenter);
		entity.setRing(ring);
		return entity;
	}

	public static CompanySiteDto mapToDto(CompanySite companySite) {
		List<PolygonDto> myPolygons = companySite.getPolygons().stream()
				.map(polygon -> EntityDtoMapper.mapToDto(polygon)).collect(Collectors.toList());
		CompanySiteDto dto = new CompanySiteDto(companySite.getId(), companySite.getTitle(), companySite.getAtDate(),
				myPolygons);
		return dto;
	}

	public static PolygonDto mapToDto(Polygon polygon) {
		List<RingDto> myRings = polygon.getRings().stream().map(ring -> EntityDtoMapper.mapToDto(ring))
				.collect(Collectors.toList());
		LocationDto centerLocationDto = polygon.getCenterLocation() == null ? null
				: EntityDtoMapper.mapToDto(polygon.getCenterLocation());
		PolygonDto dto = new PolygonDto(polygon.getId(), polygon.getFillColor(), polygon.getBorderColor(),
				polygon.getTitle(), centerLocationDto, myRings);
		return dto;
	}

	public static RingDto mapToDto(Ring ring) {
		List<LocationDto> myLocations = ring.getLocations().stream().map(location -> EntityDtoMapper.mapToDto(location))
				.collect(Collectors.toList());
		RingDto dto = new RingDto(ring.getId(), ring.isPrimaryRing(), myLocations);
		return dto;
	}

	public static LocationDto mapToDto(Location location) {
		LocationDto dto = new LocationDto(location.getId(), location.getLongitude(), location.getLatitude());
		return dto;
	}
}
