package org.unisphere.unisphere.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Map;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

public class JsonMatcher {

	private final static String JSON_ROOT = "$";
	private final StringBuilder sb = new StringBuilder();

	private JsonMatcher(String defaultPath) {
		this.sb.append(defaultPath);
	}

	private void clear() {
		sb.delete(1, sb.length());
	}

	public static JsonMatcher create() {
		return new JsonMatcher(JSON_ROOT);
	}

	public JsonMatcher clone() {
		return new JsonMatcher(sb.toString());
	}

	public JsonMatcher get(String property) {
		sb.append(".").append(property);
		return this;
	}

	public JsonMatcher at(int index) {
		sb.append("[").append(index).append("]");
		return this;
	}

	public JsonPathResultMatchers is() {
		JsonPathResultMatchers jsonPathResultMatchers = jsonPath(sb.toString());
		clear();
		return jsonPathResultMatchers;
	}

	public ResultMatcher isEquals(Object expected) {
		String result = sb.toString();
		clear();
		return jsonPath(result).value(expected);
	}

	public ResultMatcher[] isEquals(Map<String, Object> expected) {
		ResultMatcher[] matchers = expected.entrySet().stream().map(entry ->
						this.clone().get(entry.getKey()).isEquals(entry.getValue()))
				.toArray(ResultMatcher[]::new);
		clear();
		return matchers;
	}
}
