package gec.scf.dummy.config.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.headers().frameOptions().sameOrigin()
		.and().authorizeRequests()
		   .antMatchers("/", "/oauth/token", "/login", "/document/v1/***", "/img/**", "/css/**", "/js/**/**", "/favicon.ico", "/h2-console/*").permitAll()
		   .anyRequest().authenticated();
		// @formatter:on
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(null);
	}

}
