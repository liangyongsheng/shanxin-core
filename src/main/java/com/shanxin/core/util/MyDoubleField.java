package com.shanxin.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MyDoubleField<K, V> {
	private K key;
	private V value;

	public MyDoubleField() {
	};

	public MyDoubleField(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		String rs = super.toString();
		try {
			ObjectMapper om = new ObjectMapper();
			rs = om.writeValueAsString(this);
		} catch (Throwable e) {
		}
		return rs;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
