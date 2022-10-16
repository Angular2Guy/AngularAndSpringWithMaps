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

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Ring extends BaseEntity {
	private boolean primaryRing;
	@OneToMany(mappedBy = "ring", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Location> locations = new LinkedHashSet<>();
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "polygon_id")
	private Polygon polygon;

	public Set<Location> getLocations() {
		return locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	public boolean isPrimaryRing() {
		return primaryRing;
	}

	public void setPrimaryRing(boolean primaryRing) {
		this.primaryRing = primaryRing;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}
	
	
}
