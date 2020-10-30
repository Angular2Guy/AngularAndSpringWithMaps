package ch.xxx.maps.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CompanySite {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String title;
	private LocalDate atDate;
	@OneToMany(mappedBy = "companySite", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Polygon> polygons = new HashSet<>();

	public Set<Polygon> getPolygons() {
		return polygons;
	}

	public void setPolygons(Set<Polygon> polygons) {
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
}
