package ch.xxx.maps.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.xxx.maps.dto.MainConfigurationDto;

@RestController
@RequestMapping("rest/configuration")
public class ConfigurationController {
	@Value("${bing.maps-key:xxx}")
	private String mapKey;
	
	@RequestMapping(value = "/main", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MainConfigurationDto> getMainConfiguration() {
		MainConfigurationDto dto = new MainConfigurationDto(this.mapKey);
		return new ResponseEntity<MainConfigurationDto>(dto, HttpStatus.OK);
	}
}
