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
package ch.xxx.maps.domain.model.dto;

public class MainConfigurationDto {
	private String mapKey;

	public MainConfigurationDto() {
	}
	
	public MainConfigurationDto(String mapKey) {
		this.mapKey = mapKey;
	}
	
	public String getMapKey() {
		return mapKey;
	}

	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}
}
