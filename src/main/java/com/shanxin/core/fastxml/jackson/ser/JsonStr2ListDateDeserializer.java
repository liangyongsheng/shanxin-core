package com.shanxin.core.fastxml.jackson.ser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonStr2ListDateDeserializer extends JsonDeserializer<List<Date>> {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@SuppressWarnings("unchecked")
	public List<Date> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		List<Date> rs = null;
		List<String> tmps;
		try {
			List<String> justForClass = new ArrayList<String>();
			tmps = p.readValueAs(justForClass.getClass());
			if (tmps != null) {
				rs = new ArrayList<Date>();
				for (String e : tmps) {
					e = e.length() >= 10 ? e.substring(0, 10) : e;
					rs.add(sdf.parse(e));
				}
			}
		} catch (Throwable e) {
			throw new IOException(e);
		}
		return rs;
	}

}
