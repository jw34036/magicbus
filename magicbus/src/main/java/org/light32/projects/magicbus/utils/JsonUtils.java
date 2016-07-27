package org.light32.projects.magicbus.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson implementation of JSONUtils
 * 
 * @author jwhitt
 *
 */
public class JsonUtils {

	private static final ObjectMapper defaultMapper;
	
	static { 
		defaultMapper = new ObjectMapper();
		defaultMapper.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
		defaultMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	public static String toJSON(Object obj) {
		try {
			return defaultMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJSON(Class<T> klass, String jsonStr) {
		try {
			return defaultMapper.readValue(jsonStr, klass);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
