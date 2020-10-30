package ch.xxx.maps.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private BigDecimal longitude;
	private BigDecimal latitude;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ring_id")
	private Ring ring;
	@OneToOne(mappedBy = "centerLocation", fetch = FetchType.LAZY, optional = true)
	private Polygon polygonCenter;

	public Polygon getPolygonCenter() {
		return polygonCenter;
	}

	public void setPolygonCenter(Polygon polygonCenter) {
		this.polygonCenter = polygonCenter;
	}

	public Ring getRing() {
		return ring;
	}

	public void setRing(Ring ring) {
		this.ring = ring;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
