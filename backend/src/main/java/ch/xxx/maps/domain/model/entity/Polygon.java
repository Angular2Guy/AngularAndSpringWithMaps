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
package ch.xxx.maps.domain.model.entity;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Polygon extends BaseEntity {
	private String fillColor;
	private String borderColor;
	private String title;
	private BigDecimal longitude;
	private BigDecimal latitude;
	@OneToMany(mappedBy = "polygon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Ring> rings = new LinkedHashSet<>();
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_site_id")
	private CompanySite companySite;

	public CompanySite getCompanySite() {
		return companySite;
	}

	public void setCompanySite(CompanySite companySite) {
		this.companySite = companySite;
	}

	public Set<Ring> getRings() {
		return rings;
	}

	public void setRings(Set<Ring> rings) {
		this.rings = rings;
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
	
	@Override
	public int hashCode() {		
		return id != null ? Objects.hash(id) : super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		return id != null ? Objects.equals(id, other.id) : super.equals(obj);
	}
}
