package gec.scf.dummy.common.utils;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

	private static final char ESCAPED_WILDCARD_CHAR = '\\';

	/**
	 * @deprecated As of Iteration 49, because code smell use {@link #like()} instead.
	 */
	@Deprecated
	public static <V> Specification<V> like(Class<V> clazz, String fieldName,
			String value) {

		return like(fieldName, value);
	}

	public static <V> Specification<V> like(String fieldName, String value) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(value).map(SpecificationUtils::escapedWildcardValue)
				.map(val -> cb.like(root.<String>get(fieldName), "%" + val + "%",
						ESCAPED_WILDCARD_CHAR))
				.orElse(exclude());

	}

	public static <V> Specification<V> like(final SingularAttribute<V, String> fieldName,
			String value) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(value).map(SpecificationUtils::escapedWildcardValue)
				.map(val -> cb.like(root.<String>get(fieldName), "%" + val + "%",
						ESCAPED_WILDCARD_CHAR))
				.orElse(exclude());

	}

	/**
	 * @deprecated As of Iteration 49, because code smell use {@link #eq()} instead.
	 */
	@Deprecated
	public static <V, E> Specification<V> eq(Class<V> clazz, final String fieldName,
			final E value) {

		return eq(fieldName, value);
	}

	public static <V, E> Specification<V> eq(final String fieldName, final E value) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(value).map(val -> cb.equal(root.<E>get(fieldName), val))
				.orElse(exclude());

	}

	public static <V, E> Specification<V> eq(final SingularAttribute<V, E> attribute,
			final E value) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(value).map(val -> cb.equal(root.<E>get(attribute), val))
				.orElse(exclude());
	}

	/**
	 * @deprecated As of Iteration 49, because code smell use {@link #notEqual()} instead.
	 */
	@Deprecated
	public static <V, E> Specification<V> notEqual(Class<V> clazz, final String fieldName,
			final E value) {

		return notEqual(fieldName, value);
	}

	public static <V, E> Specification<V> notEqual(final String fieldName,
			final E value) {
		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(value).map(val -> cb.notEqual(root.<E>get(fieldName), val))
				.orElse(exclude());
	}

	/**
	 * @deprecated As of Iteration 49, because code smell use {@link #between()} instead.
	 */
	@Deprecated
	public static <V> Specification<V> between(Class<V> clazz, final String fieldName,
			final Date from, final Date to) {

		return between(fieldName, from, to);
	}

	public static <V> Specification<V> between(final String fieldName, final Date from,
			final Date to) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

			Predicate predicateFrom = null;
			if (from != null) {
				Calendar cal = calendar(from);
				predicateFrom = cb.greaterThanOrEqualTo(root.<Date>get(fieldName),
						cb.literal(cal.getTime()));
			}

			Predicate predicateTo = null;
			if (to != null) {
				Calendar cal = calendar(to);
				cal.add(Calendar.DATE, 1);

				predicateTo = cb.lessThan(root.<Date>get(fieldName),
						cb.literal(cal.getTime()));
			}
			if (from != null && to != null) {
				return cb.and(predicateFrom, predicateTo);
			}
			else if (from != null) {
				return predicateFrom;
			}
			else {
				return predicateTo;
			}
		};
	}

	@Deprecated
	public static <V> Specification<V> betweentime(Class<V> clazz, final String fieldName,
			final Date from, final Date to) {

		return betweentime(fieldName, from, to);
	}

	public static <V> Specification<V> betweentime(final String fieldName,
			final Date from, final Date to) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

			Predicate predicateFrom = null;
			if (from != null) {
				Calendar cal = Calendar.getInstance(Locale.US);
				cal.setTime(from);
				cal.set(Calendar.MILLISECOND, 0);
				predicateFrom = cb.greaterThanOrEqualTo(root.<Date>get(fieldName),
						cb.literal(cal.getTime()));
			}

			Predicate predicateTo = null;
			if (to != null) {
				Calendar cal = Calendar.getInstance(Locale.US);
				cal.setTime(to);
				cal.set(Calendar.MILLISECOND, 999);
				predicateTo = cb.lessThan(root.<Date>get(fieldName),
						cb.literal(cal.getTime()));
			}
			if (from != null && to != null) {
				return cb.and(predicateFrom, predicateTo);
			}
			else if (from != null) {
				return predicateFrom;
			}
			else {
				return predicateTo;
			}
		};
	}

	public static <V> Specification<V> between(SingularAttribute<V, Date> attribute,
			final Date from, final Date to) {
		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

			Predicate predicateFrom = null;
			if (from != null) {
				Calendar cal = calendar(from);

				predicateFrom = cb.greaterThanOrEqualTo(root.<Date>get(attribute),
						cb.literal(cal.getTime()));
			}

			Predicate predicateTo = null;
			if (to != null) {
				Calendar cal = calendar(to);
				cal.add(Calendar.DATE, 1);
				predicateTo = cb.lessThan(root.<Date>get(attribute),
						cb.literal(cal.getTime()));
			}
			if (from != null && to != null) {
				return cb.and(predicateFrom, predicateTo);
			}
			else if (from != null) {
				return predicateFrom;
			}
			else {
				return predicateTo;
			}
		};
	}

	public static <V> Specification<V> greaterThanOrEqual(final String fieldName,
			final Date targetDate) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(targetDate)
				.map(date -> cb.greaterThanOrEqualTo(root.<Date>get(fieldName),
						cb.literal(date)))
				.orElse(exclude());
	}

	public static <V> Specification<V> lessThanOrEqual(Class<V> clazz, String fieldName,
			Date targetDate) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(targetDate).map(SpecificationUtils::nextDay)
				.map(date -> cb.lessThan(root.<Date>get(fieldName), cb.literal(date)))
				.orElse(exclude());
	}

	public static <V> Specification<V> lessThan(Class<V> clazz, final String fieldName,
			final Date targetDate) {
		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(targetDate).map(SpecificationUtils::excludeTime)
				.map(date -> cb.lessThan(root.<Date>get(fieldName), cb.literal(date)))
				.orElse(exclude());
	}

	public static <V> Specification<V> greaterThan(final String fieldName,
			final Date targetDate) {
		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(targetDate).map(SpecificationUtils::excludeTime)
				.map(date -> cb.greaterThan(root.<Date>get(fieldName), cb.literal(date)))
				.orElse(exclude());
	}

	public static <V> Specification<V> greaterThanOrEqual(Class<V> clazz,
			String fieldName, Date targetDate) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> Optional
				.ofNullable(targetDate).map(SpecificationUtils::excludeTime)
				.map(date -> cb.or(cb.and(cb.isNull(root.<Date>get(fieldName))),
						cb.and(cb.greaterThanOrEqualTo(root.<Date>get(fieldName),
								cb.literal(date)))))
				.orElse(exclude());
	}

	/**
	 * @deprecated As of Iteration 49, because code smell use {@link #in()} instead.
	 */
	@Deprecated
	public static <V, E> Specification<V> in(Class<V> clazz, final String fieldName,
			final Collection<E> values) {

		return in(fieldName, values);

	}

	public static <V, E> Specification<V> in(final String fieldName,
			final Collection<E> values) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

			Predicate predicate = null;
			if (CollectionUtils.isNotEmpty(values)) {
				predicate = cb.in(root.<Collection<E>>get(fieldName)).value(values);
			}
			return predicate;

		};

	}

	public static <V, E> Specification<V> notIn(Class<V> clazz, final String fieldName,
			final Collection<E> values) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

			Predicate predicate = null;
			if (CollectionUtils.isNotEmpty(values)) {
				predicate = cb.not((root.<Collection<E>>get(fieldName)).in(values));
			}
			return predicate;

		};

	}

	public static <V> Specification<V> isNull(Class<V> clazz, final String fieldName) {

		return (Root<V> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb
				.isNull(root.get(fieldName));
	}


	private static String escapedWildcardValue(String value) {
		// Escape -> [] _ % ^
		value = value.replaceAll("[\\[]", "\\\\[");
		value = value.replaceAll("[\\]]", "\\\\]");
		value = value.replaceAll("[_]", "\\\\_");
		value = value.replaceAll("[%]", "\\\\%");
		value = value.replaceAll("[\\^]", "\\\\^");
		return value;
	}

	private static Predicate exclude() {
		return null;
	}

	private static Date nextDay(Date targetDate) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(targetDate);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	private static Date excludeTime(Date targetDate) {
		Calendar cal = calendar(targetDate);
		return cal.getTime();
	}

	private static Calendar calendar(final Date date) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		return DateUtils.truncate(cal, Calendar.DAY_OF_MONTH);
	}

}
