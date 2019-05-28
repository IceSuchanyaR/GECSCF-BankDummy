package gec.scf.dummy.common.resolver;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import gec.scf.dummy.common.annotation.EncryptedPathVariable;
import io.seruco.encoding.base62.Base62;

public class EncryptedPathVariableResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter
				.getParameterAnnotation(EncryptedPathVariable.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		@SuppressWarnings("rawtypes")
		Map nameValueMap = (Map) webRequest
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
		String value = (String) nameValueMap.get(parameter.getParameterName());
		if (value != null) {
			Base62 base62 = Base62.createInstance();
			final byte[] decoded = base62.decode(value.getBytes());
			value = new String(decoded);
		}
		if (parameter.getParameterType().equals(Long.class)) {
			return Long.parseLong(value);
		}
		return value;
	}

}
