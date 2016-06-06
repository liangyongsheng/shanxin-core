package com.shanxin.core.fastxml.jackson.ser;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonStr2MapDeserializer<X, Y> extends JsonDeserializer<Map<X, Y>> {
	public abstract Class<X> getXClass();

	public abstract Class<Y> getYClass();

	public abstract Date getStrDate(String arg0) throws IOException;

	@SuppressWarnings("unchecked")
	public <T> T getObject(Object value, Class<T> type) throws IOException {
		// {03/01/2016 00:00:00={RetailPrice=399, DistributionSalePrice=266...
		T rs = null;
		try {
			if (value == null)
				return rs;

			if (type.isInstance(new Date())) {
				rs = (T) getStrDate(value.toString());
			} else {
				rs = type.newInstance();
				ObjectMapper om = new ObjectMapper();
				om.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
				String tmp = om.writeValueAsString(value);
				rs = om.readValue(tmp, type);
			}
		} catch (Throwable e) {
			throw new IOException(e);
		}
		return rs;
	}

	@Override
	public Map<X, Y> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Map<X, Y> rs = null;
		try {
			Map<?, ?> tmp = p.readValueAs(Map.class);
			if (tmp == null)
				return rs;

			rs = new HashMap<X, Y>();
			Object[] objs = new Object[tmp.size()];
			objs = tmp.keySet().toArray(objs);
			for (Object e : objs)
				rs.put(getObject(e, getXClass()), getObject(tmp.get(e), getYClass()));
		} catch (Throwable e) {
			throw new IOException(e);
		}
		return rs;
	}

}
