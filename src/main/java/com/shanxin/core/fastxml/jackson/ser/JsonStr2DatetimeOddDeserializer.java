package com.shanxin.core.fastxml.jackson.ser;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonStr2DatetimeOddDeserializer extends JsonDeserializer<Date> {

	// "BeginDate": "/Date(1440518400000+0800)/",
	// "OverDate": "/Date(1443628799000+0800)/",
	@Override
	public Date deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException, JsonProcessingException {
		Date rs = null;
		try {
			String tmp = arg0.getText();
			Pattern pattern = Pattern.compile("/Date\\((\\d+)\\+\\d*\\)/");
			Matcher matcher = pattern.matcher(tmp);
			String lg = matcher.replaceAll("$1");
			rs = new Date(Long.parseLong(lg));
		} catch (Throwable e) {
			throw new IOException(e);
		}
		return rs;
	}

}
