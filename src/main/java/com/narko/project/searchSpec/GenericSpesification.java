package com.narko.project.searchSpec;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.narko.project.configuration.MasterGeneralConstants;

/**
 * 
 * @author agus.wijanarko
 *
 * @param <T>
 */

public class GenericSpesification<T> implements Specification<T> {

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	PrintStream ps = new PrintStream(baos);

	private static final long serialVersionUID = 1900581010229669687L;

	private List<Object> list;

	public GenericSpesification() {
		this.list = new ArrayList<>();
	}

	public void add(SearchCriteria criteria) {
		list.add(criteria);
		if (criteria.getLogical().equals(SearchOperation.OR_OPERATOR)
				|| criteria.getLogical().equals(SearchOperation.AND_OPERATOR)) {
			list.add(criteria.getLogical());
		}

	}

	public Predicate toPredicateQuery(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder,
			SearchCriteria criteria) {

		Predicate predicate = null;

		try {

			if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {

				if (criteria.getDataType().equals(Date.class.getName())) {

					Date date = MasterGeneralConstants.DATE_FORMAT.parse(criteria.getValue().toString());

					predicate = builder.greaterThan(root.<Date>get(criteria.getKey()), date);

				} else {
					predicate = builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());

				}
			} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
				if (criteria.getDataType().equals(Date.class.getName())) {

					Date date = MasterGeneralConstants.DATE_FORMAT.parse(criteria.getValue().toString());

					predicate = builder.lessThan(root.<Date>get(criteria.getKey()), date);

				} else {
					predicate = builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());

				}
			} else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
				if (criteria.getDataType().equals(Date.class.getName())) {

					Date date = MasterGeneralConstants.DATE_FORMAT.parse(criteria.getValue().toString());
					predicate = builder.greaterThanOrEqualTo(root.<Date>get(criteria.getKey()), date);

				} else {
					predicate = builder.greaterThanOrEqualTo(root.get(criteria.getKey()),
							criteria.getValue().toString());

				}
			} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
				if (criteria.getDataType().equals(Date.class.getName())) {

					Date date = MasterGeneralConstants.DATE_FORMAT.parse(criteria.getValue().toString());
					predicate = builder.lessThanOrEqualTo(root.<Date>get(criteria.getKey()), date);

				} else {
					predicate = builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());

				}
			} else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
				if (criteria.getDataType().equals(Date.class.getName())) {

					Date date = MasterGeneralConstants.DATE_FORMAT.parse(criteria.getValue().toString());
					predicate = builder.notEqual(root.<Date>get(criteria.getKey()), date);

				} else {
					predicate = builder.notEqual(root.get(criteria.getKey()), criteria.getValue().toString());

				}
			} else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
				if (criteria.getDataType().equals(Date.class.getName())) {

					Date date = MasterGeneralConstants.DATE_FORMAT.parse(criteria.getValue().toString());
					predicate = builder.equal(root.<Date>get(criteria.getKey()), date);

				} else {
					predicate = builder.equal(root.get(criteria.getKey()), criteria.getValue().toString());

				}
			} else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
				predicate = builder.like(root.get(criteria.getKey()),
						"%" + criteria.getValue().toString().toLowerCase() + "%");
			} else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
				predicate = builder.like(root.get(criteria.getKey()),
						criteria.getValue().toString().toLowerCase() + "%");
			} else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
				predicate = builder.like(root.get(criteria.getKey()),
						"%" + criteria.getValue().toString().toLowerCase());
			}

		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
//			message = e.getMessage();
		}
		return predicate;

	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

		Predicate predicate = null;
		Predicate leftPredicate = null;
		Predicate rightPredicate = null;
		SearchCriteria leftCriteria = null;
		SearchCriteria rightCriteria = null;
		String opt = null;
		int lenCriteria = list.size();

		if (lenCriteria == 1) {
			SearchCriteria criteria = (SearchCriteria) list.get(0);
			predicate = toPredicateQuery(root, query, builder, criteria);

		}

		else if (lenCriteria > 1) {

			for (Object predictSrc : list) {

				if (predictSrc.equals(SearchOperation.OR_OPERATOR) || predictSrc.equals(SearchOperation.AND_OPERATOR)) {
					opt = predictSrc.toString();
				}

				else if (leftCriteria == null) {
					leftCriteria = (SearchCriteria) predictSrc;
				}

				else if (rightCriteria == null) {
					rightCriteria = (SearchCriteria) predictSrc;
				}

				if (leftCriteria != null && rightCriteria != null) {

					if (opt.equals(SearchOperation.OR_OPERATOR)) {

						if (leftPredicate == null) {
							leftPredicate = toPredicateQuery(root, query, builder, leftCriteria);
						}
						rightPredicate = toPredicateQuery(root, query, builder, rightCriteria);
						predicate = builder.or(leftPredicate, rightPredicate);
						leftPredicate = predicate;

					} else if (opt.equals(SearchOperation.AND_OPERATOR)) {

						if (leftPredicate == null) {
							leftPredicate = toPredicateQuery(root, query, builder, leftCriteria);
						}
						rightPredicate = toPredicateQuery(root, query, builder, rightCriteria);
						predicate = builder.and(leftPredicate, rightPredicate);
						leftPredicate = predicate;
					}
					rightCriteria = null;
				}

			}
		}
		return predicate;
	}
}