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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.graphql.test.tester.GraphQlTester;

import ch.xxx.maps.domain.model.dto.CompanySiteDto;
import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.domain.model.entity.Location;
import ch.xxx.maps.domain.model.entity.Polygon;
import ch.xxx.maps.domain.model.entity.Ring;
import ch.xxx.maps.usecase.mapper.EntityDtoMapper;
import ch.xxx.maps.usecase.service.CompanySiteService;

@SpringBootTest
@AutoConfigureGraphQlTester
@ComponentScan(basePackages = "ch.xxx.maps"
//,excludeFilters = @Filter(type = FilterType.REGEX, pattern = ".*\\.(adapter|usecase)\\.(repository|service).*")
)
public class CompanySiteControllerTest extends BaseControllerTest {
	@MockBean
	private CompanySiteService companySiteService;
	@MockBean
	private EntityDtoMapper entityDtoMapper;
	@Autowired
	private GraphQlTester graphQlTester;

	@BeforeEach
	public void init() {
		Mockito.when(this.entityDtoMapper.mapToDto(any(CompanySite.class))).thenCallRealMethod();
	}

//	@Test
	public void getCompanySiteByIdFound() throws Exception {
		Mockito.when(this.companySiteService.findCompanySiteByIdDetached(any(Long.class), anyBoolean(), anyBoolean(),
				anyBoolean())).thenReturn(Optional.of(this.createCompanySiteEntity()));
		String myDocument = "{ getCompanySiteById(id:1) \n" + "    { id, title, atDate, \n"
				+ "      polygons { id, fillColor, borderColor, title, longitude, latitude,\n"
				+ "        rings{ id, primaryRing,\n" + " locations { id, longitude, latitude}}}}}";
		this.graphQlTester.document(myDocument).variable("id", 1).execute().path("getCompanySiteById")
				.entity(CompanySiteDto.class).satisfies(companySiteDto -> assertEquals(1L, companySiteDto.getId()));
	}

	private CompanySite createCompanySiteEntity() {
		CompanySite companySite = new CompanySite();
		companySite.setId(1L);
		companySite.setTitle("XXX");
		companySite.setAtDate(LocalDate.now());
		Location location = new Location();
		location.setId(5L);
		location.setLatitude(BigDecimal.valueOf(6L));
		location.setLongitude(BigDecimal.valueOf(7L));
		location.setOrderId(0);
		Ring ring = new Ring();
		location.setRing(ring);
		ring.setId(4L);
		ring.setPrimaryRing(true);
		ring.setLocations(Set.of(location));
		Polygon polygon = new Polygon();
		ring.setPolygon(polygon);
		polygon.setCompanySite(companySite);
		polygon.setBorderColor("aaa");
		polygon.setFillColor("bbb");
		polygon.setId(2L);
		polygon.setLatitude(BigDecimal.valueOf(2L));
		polygon.setLongitude(BigDecimal.valueOf(3L));
		polygon.setTitle("Title");
		companySite.setPolygons(Set.of(polygon));
		return companySite;
	}
}
