package gec.scf.dummy.config.server;

import java.util.List;

import gec.scf.dummy.common.resolver.EncryptedPathVariableResolver;
import gec.scf.dummy.common.resolver.PagingResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PagingResolver());
		argumentResolvers.add(new EncryptedPathVariableResolver());
	}

}
