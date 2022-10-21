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

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class CompanySite extends BaseEntity{

	private String title;
	private LocalDate atDate;
	@OneToMany(mappedBy = "companySite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Polygon> polygons = new LinkedHashSet<>();

	public Set<Polygon> getPolygons() {
		return polygons;
	}

	public void setPolygons(Set<Polygon> polygons) {
		this.polygons = polygons;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(atDate, polygons, title);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanySite other = (CompanySite) obj;
		return Objects.equals(atDate, other.atDate) && Objects.equals(polygons, other.polygons)
				&& Objects.equals(title, other.title);
	}
}
