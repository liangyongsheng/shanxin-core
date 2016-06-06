package com.shanxin.core.api;

import java.lang.reflect.Field;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.shanxin.core.exception.CheckException;
import com.shanxin.core.exception.CheckIllicitValueException;
import com.shanxin.core.exception.CheckSignException;
import com.shanxin.core.fastxml.jackson.ser.JsonStr2DatetimeDeserializer;
import com.shanxin.core.fastxml.jackson.ser.JsonStr2DatetimeSerializer;
import com.shanxin.core.util.MySignUtils;
import com.shanxin.core.util.MyStringUtils;

@JacksonXmlRootElement(localName = "apiRequest")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(creatorVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public abstract class ApiRequest<T extends ApiResponse> {
	private String method;
	@JsonSerialize(using = JsonStr2DatetimeSerializer.class)
	@JsonDeserialize(using = JsonStr2DatetimeDeserializer.class)
	private Date timestamp;
	private Integer oprtId;
	private String oprtSecret;
	private String oprtAccessToken;

	@JsonIgnore
	protected String sign;
	@JsonIgnore
	protected String messageBody;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getOprtId() {
		return oprtId;
	}

	public void setOprtId(Integer oprtId) {
		this.oprtId = oprtId;
	}

	public String getOprtSecret() {
		return oprtSecret;
	}

	public void setOprtSecret(String oprtSecret) {
		this.oprtSecret = oprtSecret;
	}

	public String getOprtAccessToken() {
		return oprtAccessToken;
	}

	public void setOprtAccessToken(String oprtAccessToken) {
		this.oprtAccessToken = oprtAccessToken;
	}

	// ---functions start here...
	public void initMessageBodySign(String messageBody, String sign) {
		this.messageBody = messageBody;
		this.sign = sign;
	}

	public abstract String getLocalMothedName();

	public abstract Class<T> getApiResponseType();

	public void checkSign() throws CheckException {
		if (!MyStringUtils.isEmpty(this.sign)) {
			String signValid = "";
			try {
				signValid = MySignUtils.signRequest(this.messageBody);
				signValid = signValid == null ? "" : signValid;
			} catch (Exception e) {
				throw new CheckSignException(e);
			}
			if (!signValid.equalsIgnoreCase(this.sign))
				throw new CheckSignException("sign is incorrect.");
			// set it null
			this.messageBody = null;
		} else
			throw new CheckSignException("sign is empty.");
	}

	public void checkSysParams() throws CheckException {
		if (MyStringUtils.isEmpty(this.method))
			throw new CheckIllicitValueException("field: method, value is empty.");
		if (this.timestamp == null)
			throw new CheckIllicitValueException("field: timestamp, value is empty.");

		// 对子类要去掉appId及appSecret验测，只需要在此子类加上一个appId_，appSecret_字段(修稀符任意，类型任意)
		if (this.oprtId == null || this.oprtId <= 0) {
			Field fd = null;
			try {
				fd = this.getClass().getDeclaredField("oprtId_");
			} catch (Throwable ex) {
			}
			if (fd == null)
				throw new CheckIllicitValueException("field: oprtId, value is illicit.");
		}

		if (MyStringUtils.isEmpty(this.oprtSecret)) {
			Field fd = null;
			try {
				fd = this.getClass().getDeclaredField("oprtSecret_");
			} catch (Throwable ex) {
			}
			if (fd == null)
				throw new CheckIllicitValueException("field: oprtSecret, value is empty.");
		}

	}

	public abstract void checkApiParams() throws CheckException;
}
