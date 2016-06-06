package com.shanxin.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MyTripleField<K, V, A> {
	private K key;
	private V value;
	private A append;

	public MyTripleField() {
	};

	public MyTripleField(K key, V value, A append) {
		this.key = key;
		this.value = value;
		this.append = append;
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

	public A getAppend() {
		return append;
	}

	public void setAppend(A append) {
		this.append = append;
	}
}
