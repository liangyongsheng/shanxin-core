package com.shanxin.core.fastxml.jackson.ser;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public abstract class JsonStr2MapSerializer<X, Y> extends JsonSerializer<Map<X, Y>> {
	public abstract Class<X> getXClass();

	public abstract Class<Y> getYClass();

	public abstract String getStrDate(Date arg0) throws IOException;

	@Override
	public void serialize(Map<X, Y> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		try {
			Map<?, ?> tMap = value;
			if (getXClass().isInstance(new Date())) {
				Map<String, Y> tmpMap = new HashMap<String, Y>();
				Object[] objs = new Object[value.size()];
				objs = value.keySet().toArray(objs);
				for (Object obj : objs)
					tmpMap.put(getStrDate((Date) obj), value.get(obj));
				tMap = tmpMap;
			}
			gen.writeObject(tMap);
		} catch (Throwable e) {
			throw new IOException(e);
		}
	}

}
