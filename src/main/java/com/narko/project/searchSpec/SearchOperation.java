package com.narko.project.searchSpec;

/**
 * 
 * @author agus.wijanarko
 *
 * @param <T>
 */

public enum SearchOperation {
	GREATER_THAN, LESS_THAN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL, NOT_EQUAL, EQUAL, MATCH, MATCH_END, MATCH_START,
	EQUALITY, NEGATION, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;

	public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~" };

	public static final String[] SIMPLE_LOGIC_SET = { "OR", "AND", ";", "or", "and" };

	// public static final String OR_PREDICATE_FLAG = "'";

	// public static final String ZERO_OR_MORE_REGEX = "*";

	public static final String OR_OPERATOR = "OR";

	public static final String AND_OPERATOR = "AND";

	public static final String LEFT_PARANTHESIS = "(";

	public static final String RIGHT_PARANTHESIS = ")";

	public static SearchOperation getSimpleOperation(final String input) {
		switch (input) {
		case ":":
			return EQUAL;
		case "!":
			return NOT_EQUAL;
		case ">":
			return GREATER_THAN_EQUAL;
		case "<":
			return LESS_THAN_EQUAL;
		case "~":
			return MATCH;
		default:
			return null;
		}
	}
}