package ch.xxx.maps.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Polygon {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String fillColor;
	private String borderColor;
	private String title;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "center_location")
	private Location centerLocation;
	@OneToMany(mappedBy = "polygon", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Ring> rings = new HashSet<>();
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

	public Location getCenterLocation() {
		return centerLocation;
	}

	public void setCenterLocation(Location centerLocation) {
		this.centerLocation = centerLocation;
	}
}
