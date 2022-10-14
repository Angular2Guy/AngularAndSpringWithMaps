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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import ch.xxx.maps.domain.model.dto.MainConfigurationDto;

@Controller
public class ConfigurationController {
	@Value("${bing.maps-key}")
	private String mapKey;
	
	@QueryMapping
	public MainConfigurationDto getMainConfiguration() {
		MainConfigurationDto dto = new MainConfigurationDto(this.mapKey);
		return dto;
	}
}
