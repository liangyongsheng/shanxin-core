package com.shanxin.core.fastxml.jackson.ser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonStr2ListDateSerializer extends JsonSerializer<List<Date>> {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void serialize(List<Date> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		try {
			if (value == null)
				gen.writeNull();
			else {
				gen.writeStartArray();
				for (Date e : value)
					gen.writeObject(sdf.format(e));
				gen.writeEndArray();
			}
		} catch (Throwable e) {
			throw new IOException(e);
		}
	}
}
