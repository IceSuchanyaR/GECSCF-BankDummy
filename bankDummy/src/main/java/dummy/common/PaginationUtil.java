package gec.scf.dummy.common;

import java.net.URISyntaxException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

public class PaginationUtil {

	public static final int DEFAULT_OFFSET = 1;

	public static final int MIN_OFFSET = 1;

	public static final int DEFAULT_LIMIT = 20;

	public static final int MAX_LIMIT = 100;

	public static Pageable generatePageRequest(Integer offset, Integer limit) {
		if (offset == null || offset < MIN_OFFSET) {
			offset = DEFAULT_OFFSET;
		}
		if (limit == null || limit > MAX_LIMIT) {
			limit = DEFAULT_LIMIT;
		}
		return new PageRequest(offset - 1, limit);
	}

	private PaginationUtil() {
		throw new IllegalStateException("Utility class");

	}

	/**
	 * @deprecated (Use {@link PaginationUtil#generatePaginationHttpHeaders(Page)}
	 * instead)
	 * @param page
	 * @param offset
	 * @param limit
	 * @return
	 * @throws URISyntaxException
	 */
	@Deprecated
	public static HttpHeaders generatePaginationHttpHeaders(Page<?> page, Integer offset,
			Integer limit) throws URISyntaxException {

		return generatePaginationHttpHeaders(page);
	}

	public static HttpHeaders generatePaginationHttpHeaders(Page<?> page)
			throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Page-Size", String.valueOf(page.getSize()));
		headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
		headers.add("X-Total-Page", String.valueOf(page.getTotalPages()));

		return headers;
	}

}
