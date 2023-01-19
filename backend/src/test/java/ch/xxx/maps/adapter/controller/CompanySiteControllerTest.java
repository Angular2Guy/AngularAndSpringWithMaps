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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.usecase.mapper.EntityDtoMapper;
import ch.xxx.maps.usecase.service.CompanySiteService;

@WebMvcTest(CompanySiteController.class)
@ComponentScan(basePackages = "ch.xxx.maps", excludeFilters = @Filter(type = FilterType.REGEX, pattern = ".*\\.(adapter|usecase)\\.(repository|service).*"))
public class CompanySiteControllerTest {
	@MockBean
	private CompanySiteService companySiteService;
	@MockBean
	private EntityDtoMapper entityDtoMapper;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		Mockito.when(this.entityDtoMapper.mapToDto(any(CompanySite.class))).thenCallRealMethod();
	}

	@Test
	public void getCompanySiteByIdFound() throws Exception {
		Mockito.when(this.companySiteService.findCompanySiteByIdDetached(any(Long.class), anyBoolean(), anyBoolean(),anyBoolean()))
				.thenReturn(Optional.of(this.createCompanySiteEntity()));
		this.mockMvc.perform(post("/graphql").contentType(MediaType.APPLICATION_JSON).servletPath("/graphql")
				.content("{ getCompanySiteById(id:1) \n" + "    { id, title, atDate, \n"
						+ "      polygons { id, fillColor, borderColor, title, longitude, latitude,\n"
						+ "        rings{ id, primaryRing,\n" + "          locations { id, longitude, latitude}}}}}"))
				.andExpect(status().isNotFound());
	}

	private CompanySite createCompanySiteEntity() {
		CompanySite companySite = new CompanySite();
		companySite.setId(1L);
		companySite.setTitle("XXX");
		return companySite;
	}
}
