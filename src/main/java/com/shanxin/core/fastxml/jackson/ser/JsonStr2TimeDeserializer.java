package com.shanxin.core.fastxml.jackson.ser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonStr2TimeDeserializer extends JsonDeserializer<Date> {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Date rs = null;
		try {
			String tmp = p.getText();
			tmp = tmp.length() >= 8 ? tmp.substring(0, 8) : tmp;
			tmp = tmp.length() == 3 || tmp.length() == 4 || tmp.length() == 5 ? tmp + ":00" : tmp;
			tmp = sdfDate.format(new Date()) + " " + tmp;
			rs = sdf.parse(tmp);
		} catch (Throwable e) {
			throw new IOException(e);
		}
		return rs;
	}

}
