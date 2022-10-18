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
package ch.xxx.maps.adapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import ch.xxx.maps.adapter.repository.JpaCompanySiteRepository;
import ch.xxx.maps.adapter.repository.JpaLocationRepository;
import ch.xxx.maps.adapter.repository.JpaPolygonRepository;
import ch.xxx.maps.adapter.repository.JpaRingRepository;
import ch.xxx.maps.domain.model.entity.CompanySite;
import ch.xxx.maps.domain.model.entity.Location;
import ch.xxx.maps.domain.model.entity.Polygon;
import ch.xxx.maps.domain.model.entity.Ring;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetcher;
 
@Configuration
public class GraphqlConfig {

	@Bean
	public RuntimeWiringConfigurer runtimeWiringConfigurer(JpaCompanySiteRepository jpaCompanySiteRepository, 
			JpaPolygonRepository jpaPolygonRepository, JpaRingRepository jpaRingRepository, JpaLocationRepository jpaLocationRepository) {
		DataFetcher<Iterable<CompanySite>> dataFetcherCs = QuerydslDataFetcher.builder(jpaCompanySiteRepository).many();
		DataFetcher<Iterable<Polygon>> dataFetcherPg = QuerydslDataFetcher.builder(jpaPolygonRepository).many();
		DataFetcher<Iterable<Ring>> dataFetcherRi = QuerydslDataFetcher.builder(jpaRingRepository).many();
		DataFetcher<Iterable<Location>> dataFetcherLo = QuerydslDataFetcher.builder(jpaLocationRepository).many();
		RuntimeWiringConfigurer result = wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Date)
				.scalar(ExtendedScalars.DateTime).scalar(ExtendedScalars.GraphQLBigDecimal)
				.scalar(ExtendedScalars.GraphQLBigInteger).scalar(ExtendedScalars.GraphQLByte)
				.scalar(ExtendedScalars.GraphQLChar).scalar(ExtendedScalars.GraphQLLong)
				.scalar(ExtendedScalars.GraphQLShort).scalar(ExtendedScalars.Json).scalar(ExtendedScalars.Locale)
				.scalar(ExtendedScalars.LocalTime).scalar(ExtendedScalars.NegativeFloat)
				.scalar(ExtendedScalars.NegativeInt).scalar(ExtendedScalars.NonNegativeFloat)
				.scalar(ExtendedScalars.NonNegativeInt).scalar(ExtendedScalars.NonPositiveFloat)
				.scalar(ExtendedScalars.NonPositiveInt).scalar(ExtendedScalars.Object).scalar(ExtendedScalars.Time)
				.scalar(ExtendedScalars.Url).scalar(ExtendedScalars.UUID)
				.type("Query", builder -> builder.dataFetcher("companySiteIn", dataFetcherCs).dataFetcher("polygonIn", dataFetcherPg)
						.dataFetcher("ringIn", dataFetcherRi).dataFetcher("locationIn", dataFetcherLo));
		return result;
	}
}
