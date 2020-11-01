package ch.xxx.maps.dto;

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
