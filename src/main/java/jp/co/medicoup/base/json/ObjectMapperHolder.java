package jp.co.medicoup.base.json;

import java.util.TimeZone;

import jp.co.medicoup.base.AppConstants;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 *
 * @author izumi_j
 *
 */
public final class ObjectMapperHolder {
	private ObjectMapperHolder() {
	}

	private static final ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		mapper.setTimeZone(TimeZone.getTimeZone(AppConstants.TIMEZONE_ID_JST));
		mapper.registerModule(new JodaModule());
	}

	public static ObjectMapper get() {
		return mapper;
	}
}
