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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PolygonDto {
	private Long id;
	private String fillColor;
	private String borderColor;
	private String title;
	private BigDecimal longitude;
	private BigDecimal latitude;
	private List<RingDto> rings = new ArrayList<>();

	public PolygonDto() {
	}

	public PolygonDto(Long id, String fillColor, String borderColor, String title, BigDecimal longitude,
			BigDecimal latitude, List<RingDto> rings) {
		super();
		this.id = id;
		this.fillColor = fillColor;
		this.borderColor = borderColor;
		this.title = title;
		this.longitude = longitude;
		this.latitude = latitude;
		this.rings = rings;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<RingDto> getRings() {
		return rings;
	}

	public void setRings(List<RingDto> rings) {
		this.rings = rings;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
}
