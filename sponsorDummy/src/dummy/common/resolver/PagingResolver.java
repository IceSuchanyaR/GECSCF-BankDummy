package gec.scf.dummy.common.resolver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import gec.scf.dummy.common.utils.OffsetBasedPageRequest;

public class PagingResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Pageable.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {

		String sortingParam = webRequest.getParameter("sort");

		String pageParam = webRequest.getParameter("page");
		String sizeParam = webRequest.getParameter("size");

		String offsetParam = webRequest.getParameter("offset");
		String limitParam = webRequest.getParameter("limit");

		String[] sortings = StringUtils.split(sortingParam, ',');

		Sort sort = Sort.unsorted();
		if (sortings != null && sortings.length > 0) {

			final String startWithMinusRegex = "(-)\\w+";

			List<Sort.Order> orders = Arrays.asList(sortings).stream().map(sortField -> {
				if (Pattern.matches(startWithMinusRegex, sortField)) {
					return new Sort.Order(Direction.DESC, sortField.replaceAll("-", ""));
				}
				else {
					return new Sort.Order(Direction.ASC, sortField);
				}
			}).collect(Collectors.toList());

			sort = Sort.by(orders);
		}

		if (StringUtils.isNotBlank(limitParam)) {
			int offset = Integer.parseInt(Optional.ofNullable(offsetParam).orElse("0"));
			int limit = Integer.MAX_VALUE;
			if (StringUtils.isNotBlank(limitParam)) {
				limit = Integer.parseInt(limitParam);
			}
			return new OffsetBasedPageRequest(offset, limit, sort);
		}
		else {
			int page = Integer.parseInt(Optional.ofNullable(pageParam).orElse("0"));
			int size = Integer.MAX_VALUE;
			if (StringUtils.isNotBlank(sizeParam)) {
				size = Integer.parseInt(sizeParam);
			}
			return PageRequest.of(page, size, sort);
		}

	}

}
