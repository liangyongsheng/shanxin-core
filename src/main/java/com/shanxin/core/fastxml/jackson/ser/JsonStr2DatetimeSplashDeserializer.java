package com.shanxin.core.fastxml.jackson.ser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonStr2DatetimeSplashDeserializer extends JsonDeserializer<Date> {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private SimpleDateFormat sdfConn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Date rs = null;
		try {
			// 2011-03-14T19:20:00+08:00
			String tmp = p.getText();
			if (tmp.indexOf("T") >= 0 || tmp.length() >= 19) {
				tmp = tmp.substring(0, 19);
				tmp = tmp.replace("T", " ");
			}
			if (tmp.indexOf("/") >= 0)
				rs = sdf.parse(tmp);
			else
				rs = sdfConn.parse(tmp);
		} catch (Throwable e) {
			throw new IOException(e);
		}
		return rs;
	}

}
