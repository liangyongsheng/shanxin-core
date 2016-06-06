package com.shanxin.core.fastxml.urlencoded;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shanxin.core.util.MyStringUtils;

public class UrlEncodedMapper {
	private static List<Class<?>> genenicClasses = new ArrayList<Class<?>>();
	private static Map<String, Class<?>> mapGenenicClasses = new HashMap<String, Class<?>>();

	// 方法1，这个估计
	public void addGenenicClass(Class<?> genenicClass) {
		if (genenicClass.getName().startsWith("java."))
			return;
		UrlEncodedMapper.genenicClasses.add(genenicClass);
	}

	// 方法2，这个比效精准
	public void putGenenicClass(String fieldName, Class<?> genenicClass) {
		if (MyStringUtils.isEmpty(fieldName) || genenicClass == null)
			return;
		if (genenicClass.getName().startsWith("java."))
			return;
		UrlEncodedMapper.mapGenenicClasses.put(fieldName, genenicClass);
	}

	public String writeValueAsString(Object value) throws IOException {
		if (value == null)
			return null;
		// TODO writeValueAsString
		throw new IOException("not implement yet.");
	}

	public <T> T readValue(String urlEncodedStr, Class<T> valueType) throws IOException {
		try {
			Map<String, Object> kvMap = initKvMap(urlEncodedStr, valueType);
			if (kvMap == null)
				return null;
			ObjectMapper om = new ObjectMapper();
			String json = om.writeValueAsString(kvMap);
			T rs = om.readValue(json, valueType);
			return rs;
		} catch (Throwable ex) {
			throw new IOException(ex);
		}
	}

	private static Map<String, Object> initKvMap(String urlEncodedStr, Class<?> targetClass) throws IOException {
		// 此Object 为String 或 List<String> 或 Map<String,List<Map<String,List<Map<String,List..>>>
		Map<String, Object> rs = new HashMap<String, Object>();
		if (urlEncodedStr == null || targetClass == null)
			return null;

		try {
			Map<String, Object> kvMapFilter1 = new HashMap<String, Object>();
			Map<String, Object> kvMapFilter2 = new HashMap<String, Object>();

			// 一、kvMapFilter1 完成url===> k-v对
			String[] kvStrs = urlEncodedStr.split("&");
			for (String e : kvStrs) {
				String[] kvArr = e.split("=");
				if (kvArr.length <= 0)
					continue;
				if (MyStringUtils.isEmpty(kvArr[0]))
					continue;

				String key = kvArr[0];
				String value = null;
				if (kvArr.length == 2)
					value = kvArr[1];
				else if (kvArr.length > 2) {
					value = e.substring(e.indexOf("=") + 1);
				} else if (e.indexOf("=") >= 0)
					value = "";

				if (value == null)
					continue;

				key = URLDecoder.decode(key, "utf-8");
				value = URLDecoder.decode(value, "utf-8");

				if (kvMapFilter1.containsKey(key)) {
					Object vObj = kvMapFilter1.get(key);
					if (vObj instanceof String) {
						List<String> vs = new ArrayList<String>();
						vs.add((String) vObj);
						vs.add(value);
						kvMapFilter1.put(key, vs);
					} else if (vObj instanceof List) {
						@SuppressWarnings("unchecked")
						List<String> list = (List<String>) kvMapFilter1.get(key);
						list.add(value);
					} else
						throw new Exception("此情况是不存在的。");

				} else
					kvMapFilter1.put(key, value);
			}

			// ***取的目标类字段field的对象存在=>fMap
			Map<String, Field> fMap = new HashMap<String, Field>();
			@SuppressWarnings("rawtypes")
			List<Class> cs = new ArrayList<Class>();
			cs.add(targetClass);
			Class<?> beginClass = targetClass;
			while (beginClass.getSuperclass() != null && !beginClass.getSuperclass().getName().equals("java.lang.Object")) {
				cs.add(beginClass.getSuperclass());
				beginClass = beginClass.getSuperclass();
			}
			for (Class<?> e : cs) {
				Field[] tmpFs = e.getDeclaredFields();
				for (Field f : tmpFs) {
					if (fMap.containsKey(f.getName()))
						continue;
					else
						fMap.put(f.getName(), f);
				}
			}

			// 二、kvMapFilter2，修正key大小写（Key是由url读取的，现变为由field读取）
			// kvMapFilter1 => kvMapFilter2
			Iterator<String> iterator = kvMapFilter1.keySet().iterator();
			while (iterator.hasNext()) {
				String key1 = iterator.next();
				String key2 = null;
				// 修正key大小写（Key是由url读取的，现变为由field读取）
				Iterator<Field> tmpIr = fMap.values().iterator();
				while (tmpIr.hasNext()) {
					Field fd = tmpIr.next();
					if (fd.getName().equalsIgnoreCase(key1))
						key2 = fd.getName();
				}

				String key = key2 == null ? key1 : key2;
				Object value = kvMapFilter1.get(key1);
				if (kvMapFilter2.containsKey(key)) {
					if (kvMapFilter2.get(key) instanceof String) {
						List<String> tmpLs = new ArrayList<String>();
						tmpLs.add(kvMapFilter2.get(key).toString());

						if (value instanceof String) {
							tmpLs.add(value.toString());
							kvMapFilter2.put(key, tmpLs);
						} else if (value instanceof List) {
							@SuppressWarnings("unchecked")
							List<String> tmpLsAnother = (List<String>) value;
							for (String e : tmpLsAnother)
								tmpLs.add(e);
							kvMapFilter2.put(key, tmpLs);
						} else
							throw new Exception("此情况是不存在的。");

					} else if (kvMapFilter2.get(key) instanceof List) {
						@SuppressWarnings("unchecked")
						List<String> tmpLs = (List<String>) kvMapFilter2.get(key);

						if (value instanceof String) {
							tmpLs.add(value.toString());
							kvMapFilter2.put(key, tmpLs);
						} else if (value instanceof List) {
							@SuppressWarnings("unchecked")
							List<String> tmpLsAnother = (List<String>) value;
							for (String e : tmpLsAnother)
								tmpLs.add(e);
							kvMapFilter2.put(key, tmpLs);
						} else
							throw new Exception("此情况是不存在的。");

					} else
						throw new Exception("此情况是不存在的。");
				} else
					kvMapFilter2.put(key, value);
			}

			// 三、kvMapFilter2的稀选 ==> rs
			// 1、目标为列表List,Set,Array
			// 2、XXX 目标为Map，不处理
			// 3、目标Number
			// 4、目标为java.*下的类
			// 5、目标为用户类
			Iterator<String> it = kvMapFilter2.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (!fMap.containsKey(key))
					continue;

				Field f = fMap.get(key);
				Class<?> c = f.getType();

				// 1、目标为列表List,Set,Array
				if (List.class.isAssignableFrom(c) || Set.class.isAssignableFrom(c) || c.isArray()) {
					// 若String修正为List
					if (kvMapFilter2.get(key) instanceof String) {
						List<String> tmpLs = new ArrayList<String>();
						tmpLs.add(kvMapFilter2.get(key).toString());
						kvMapFilter2.put(key, tmpLs);
					}
					// List<E>中的E的元数据已掉了，只能一次处理UrlEncodedMapper.mapGenenicClasses或UrlEncodedMapper.genenicClasses记住其泛型类型
					try {
						// 1、map比效精准，2、list估计
						@SuppressWarnings("unchecked")
						List<String> lsTmp = (List<String>) kvMapFilter2.get(key);
						List<Map<String, Object>> lsMap = new ArrayList<Map<String, Object>>();
						// map比效精准
						if (UrlEncodedMapper.mapGenenicClasses.containsKey(key)) {
							for (String e : lsTmp)
								lsMap.add(initKvMap(e, UrlEncodedMapper.mapGenenicClasses.get(key)));
							// 估计是否为客户类
							boolean isAllZ = true;
							for (Map<String, Object> e : lsMap) {
								if (e.size() > 0) {
									isAllZ = false;
									break;
								}
							}
							if (isAllZ == true)
								rs.put(key, kvMapFilter2.get(key));
							else
								rs.put(key, lsMap);
						}
						// list估计
						else if (UrlEncodedMapper.genenicClasses.size() > 0) {
							int fieldMaxCount = 0;
							List<Map<String, Object>> theTmpLM = new ArrayList<Map<String, Object>>();
							for (Class<?> cc : UrlEncodedMapper.genenicClasses) {
								List<Map<String, Object>> tmpLM = new ArrayList<Map<String, Object>>();
								for (String e : lsTmp)
									tmpLM.add(initKvMap(e, cc));

								int count = 0;
								for (Map<String, Object> e : tmpLM)
									count += e.size();
								if (count > fieldMaxCount) {
									fieldMaxCount = count;
									theTmpLM = tmpLM;
								}
							}
							// 估计是否为客户类
							boolean isAllZ = true;
							for (Map<String, Object> e : theTmpLM) {
								if (e.size() > 0) {
									isAllZ = false;
									break;
								}
							}
							if (isAllZ == true)
								rs.put(key, kvMapFilter2.get(key));
							else
								rs.put(key, theTmpLM);
						} else
							rs.put(key, kvMapFilter2.get(key));
					} catch (Throwable ex) {
					}
				}
				// 目标字段是map
				else if (Map.class.isAssignableFrom(c)) {
					// 求解********
					// XXX Map不知如何处，这里直接把此k-v去掉
					// **************
				}
				// 目标字段是Number
				else if (Number.class.isAssignableFrom(c)) {
					// 可以处理空null值的Number(fastxml.jackson)
					if ((kvMapFilter2.get(key) instanceof String)) {
						rs.put(key, kvMapFilter2.get(key));
					} else {
						@SuppressWarnings("unchecked")
						List<String> tmpLs = (List<String>) kvMapFilter2.get(key);
						rs.put(key, tmpLs.get(tmpLs.size() - 1));
					}
				} else if (c.getName().equals("int") || c.getName().equals("long") || c.getName().equals("float") || c.getName().equals("double")) {
					if ((kvMapFilter2.get(key) instanceof String)) {
						if (MyStringUtils.isEmpty(kvMapFilter2.get(key).toString())) {
							// int, double等立即数是不可处理空null值，去掉此k-v对
						} else
							rs.put(key, kvMapFilter2.get(key));
					} else {
						@SuppressWarnings("unchecked")
						List<String> tmpLs = (List<String>) kvMapFilter2.get(key);
						if (MyStringUtils.isEmpty(tmpLs.get(tmpLs.size() - 1).toString())) {
							// int, double等立即数是不可处理空null值，去掉此k-v对
						} else
							rs.put(key, tmpLs.get(tmpLs.size() - 1));
					}
				}
				// java.*定义类
				else if (c.getName().startsWith("java.")) {
					if ((kvMapFilter2.get(key) instanceof List)) {
						@SuppressWarnings("unchecked")
						List<String> tmpLs = (List<String>) kvMapFilter2.get(key);
						rs.put(key, tmpLs.get(tmpLs.size() - 1));
					} else
						rs.put(key, kvMapFilter2.get(key).toString());

				}
				// 客户类
				else {
					if ((kvMapFilter2.get(key) instanceof List)) {
						@SuppressWarnings("unchecked")
						List<String> tmpLs = (List<String>) kvMapFilter2.get(key);
						rs.put(key, initKvMap(tmpLs.get(tmpLs.size() - 1), c));
					} else
						rs.put(key, initKvMap(kvMapFilter2.get(key).toString(), c));
				}
			}

			return rs;
		} catch (Throwable ex) {
			throw new IOException(ex);
		}
	}
}
