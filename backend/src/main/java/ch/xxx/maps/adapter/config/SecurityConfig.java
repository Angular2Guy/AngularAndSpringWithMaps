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
package ch.xxx.maps.adapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		HttpSecurity result = http
				.authorizeHttpRequests(
						authorize -> authorize.requestMatchers("/**").permitAll())
				.csrf(myCsrf -> myCsrf.disable())
				.sessionManagement(mySm -> mySm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers(myHeaders -> myHeaders.contentSecurityPolicy(myCsp -> myCsp.policyDirectives(
						"default-src 'self' data: https://*.virtualearth.net https://*.bing.com; script-src 'self' https://*.virtualearth.net "
								+ " https://*.bing.com 'unsafe-inline'; style-src 'self' https://*.bing.com 'unsafe-inline'; font-src 'self' "
								+ " data: https://fonts.gstatic.com;")))
				.headers(myHeaders -> myHeaders.xssProtection(myXss -> myXss.headerValue(HeaderValue.ENABLED)))
				.headers(myHeaders -> myHeaders.frameOptions(myFo -> myFo.sameOrigin()));
		return result.build();
	}
}
