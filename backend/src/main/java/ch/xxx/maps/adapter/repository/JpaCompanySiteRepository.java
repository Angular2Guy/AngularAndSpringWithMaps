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
package ch.xxx.maps.adapter.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import ch.xxx.maps.domain.model.entity.CompanySite;

public interface JpaCompanySiteRepository extends JpaRepository<CompanySite, Long>, QuerydslPredicateExecutor<CompanySite> {
	@Query("select cs from CompanySite cs where lower(cs.title) like %:title% and cs.atDate >= :from and cs.atDate <= :to")
	List<CompanySite> findByTitleFromTo(@Param("title") String title, @Param("from") LocalDate from,  @Param("to") LocalDate to);
	@Query("select distinct cs from CompanySite cs inner join fetch cs.polygons p inner join fetch p.rings r inner join fetch r.locations l where lower(cs.title) like %:title% and cs.atDate >= :from and cs.atDate <= :to")
	List<CompanySite> findByTitleFromToWithChildren(@Param("title") String title, @Param("from") LocalDate from,  @Param("to") LocalDate to);
	@Query(value = "select distinct cs from CompanySite cs join fetch cs.polygons p join fetch p.rings r join fetch r.locations l where cs.id = :id")
	Optional<CompanySite> findByIdWithChildren(@Param("id") Long id);
}
