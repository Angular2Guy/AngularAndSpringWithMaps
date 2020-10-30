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
package ch.xxx.maps.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompanySiteDto {
	private Long id;
	private String title;
	private LocalDate atDate;
	private List<PolygonDto> polygons = new ArrayList<>();

	public CompanySiteDto() {
	}

	public CompanySiteDto(Long id, String title, LocalDate atDate, List<PolygonDto> polygons) {
		super();
		this.id = id;
		this.title = title;
		this.atDate = atDate;
		this.polygons = polygons;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getAtDate() {
		return atDate;
	}

	public void setAtDate(LocalDate atDate) {
		this.atDate = atDate;
	}

	public List<PolygonDto> getPolygons() {
		return polygons;
	}

	public void setPolygons(List<PolygonDto> polygons) {
		this.polygons = polygons;
	}
}
