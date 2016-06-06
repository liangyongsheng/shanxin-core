package com.shanxin.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shanxin.core.exception.CoreException;

public class MyObj2StrUtils {
	/**
	 * when (len <= 0) is original string
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String cutString(String str, int len) {
		if (str != null && len > 0 && str.length() > len)
			str = str.substring(0, len);
		return str;
	}

	/**
	 * when (len <= 0) is original string
	 * 
	 * @param obj
	 * @param len
	 * @return
	 */
	public static String toJson(Object obj, int len) {
		String rs = null;
		if (obj == null)
			return rs;
		try {
			ObjectMapper om = new ObjectMapper();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			om.setDateFormat(sdf);
			rs = om.writeValueAsString(obj);
			if (rs != null && len > 0 && rs.length() > len)
				rs = rs.substring(0, len);
		} catch (Throwable e) {
			rs = obj.toString();
		}
		return rs;
	}

	public static String toJson(Object obj, int len, DateFormat df) {
		String rs = null;
		if (obj == null)
			return rs;
		try {
			ObjectMapper om = new ObjectMapper();
			if (df != null)
				om.setDateFormat(df);
			rs = om.writeValueAsString(obj);
			if (rs != null && len > 0 && rs.length() > len)
				rs = rs.substring(0, len);
		} catch (Throwable e) {
			rs = obj.toString();
		}
		return rs;
	}

	/**
	 * 
	 * @param json
	 * @param type
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> type) throws CoreException {
		T rs = null;
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			rs = (T) om.readValue(json, type);
		} catch (Throwable e) {
			throw new CoreException(e);
		}
		return rs;
	}
}
