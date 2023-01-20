/**
 *    Copyright 2016 Sven Loesekann

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
package ch.xxx.maps.usecase.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.domain.model.entity.CompanySiteRepository;
import ch.xxx.maps.domain.model.entity.LocationRepository;
import ch.xxx.maps.domain.model.entity.PolygonRepository;
import ch.xxx.maps.domain.model.entity.RingRepository;
import jakarta.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
public class CompanySiteServiceTest {
	@Mock
	private CompanySiteRepository companySiteRepository;
	@Mock
	private PolygonRepository polygonRepository;
	@Mock
	private RingRepository ringRepository;
	@Mock
	private LocationRepository locationRepository;
	@Mock
	private EntityManager entityManager;
	@InjectMocks
	private CompanySiteService companySiteService;
	
	@Test
	public void findByCompanySiteIdFound() throws Exception {
		CompanySite companySite = this.createCompanySiteEntity();
		Mockito.when(this.companySiteRepository.findByIdWithChildren(any(Long.class))).thenReturn(Optional.of(companySite)) ;
		Optional<CompanySite> companySiteOpt = this.companySiteService.findCompanySiteById(1L);
		Assertions.assertTrue(companySiteOpt.isPresent());
		Assertions.assertEquals(companySite.getId(), companySiteOpt.get().getId());
		Assertions.assertEquals(companySite.getTitle(), companySiteOpt.get().getTitle());
	}
	
	@Test
	public void findByCompanySiteIdNotFound() throws Exception {
		Mockito.when(this.companySiteRepository.findByIdWithChildren(any(Long.class))).thenReturn(Optional.empty()) ;
		Optional<CompanySite> companySiteOpt = this.companySiteService.findCompanySiteById(-1L);
		Assertions.assertTrue(companySiteOpt.isEmpty());
	}
	
	private CompanySite createCompanySiteEntity() {
		CompanySite companySite = new CompanySite();
		companySite.setId(1L);
		companySite.setTitle("XXX");
		return companySite;
	}
}
