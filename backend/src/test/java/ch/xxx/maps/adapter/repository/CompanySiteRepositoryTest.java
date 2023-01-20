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
package ch.xxx.maps.adapter.repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.domain.model.entity.CompanySiteRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CompanySiteRepositoryTest {
	@Autowired
	private CompanySiteRepository companySiteRepository;
	
	@Test
	public void findCompanySiteByIdFound() throws Exception {
		final Long companySiteId = 1L;
		Optional<CompanySite> companySiteOpt = this.companySiteRepository.findByIdWithChildren(companySiteId);
		Assertions.assertTrue(companySiteOpt.isPresent());
		Assertions.assertEquals(companySiteOpt.get().getId(), companySiteId);
		Assertions.assertFalse(companySiteOpt.get().getPolygons().isEmpty());
	}
	
	@Test
	public void findCompanySiteByIdNotFound() throws Exception {
		final Long companySiteId = -1L;
		Optional<CompanySite> companySiteOpt = this.companySiteRepository.findByIdWithChildren(companySiteId);
		Assertions.assertTrue(companySiteOpt.isEmpty());
	}
}
