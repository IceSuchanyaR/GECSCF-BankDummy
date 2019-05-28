package gec.scf.dummy.common.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import gec.scf.dummy.common.annotation.EncryptedRequestParam;
import io.seruco.encoding.base62.Base62;

public class EncryptedRequestParamResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter
				.getParameterAnnotation(EncryptedRequestParam.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		String value = webRequest.getParameter(parameter.getParameterName());
		if (StringUtils.isNoneBlank(value)) {
			Base62 base62 = Base62.createInstance();
			final byte[] decoded = base62.decode(value.getBytes());
			value = new String(decoded);
		}
		else {
			if (parameter.getParameterAnnotation(EncryptedRequestParam.class)
					.required()) {
				return WebArgumentResolver.UNRESOLVED;
			}
		}
		if (parameter.getParameterType().equals(Long.class)) {
			return Long.parseLong(value);
		}
		return value;
	}

}
