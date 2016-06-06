package com.shanxin.core.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.shanxin.core.exception.CheckException;
import com.shanxin.core.exception.InitException;
import com.shanxin.core.exception.ServiceException;
import com.shanxin.core.fastxml.urlencoded.UrlEncodedMapper;

@Service
public abstract class ApiBo<T extends ApiRequest<?>> {
	private boolean needCheckSign;
	protected T apiRequest;
	protected ApiResponse apiResponse;

	public ApiResponse getApiResponse() {
		return this.apiResponse;
	}

	public T getApiRequest() {
		return this.apiRequest;
	}

	public abstract Class<T> getApiRequestType();

	// 1.
	public void doInit(T apiRequest) throws InitException {
		this.needCheckSign = false;
		try {
			this.apiRequest = apiRequest;
			if (this.apiRequest == null)
				throw new Exception("no such request-object.");
			this.apiResponse = this.apiRequest.getApiResponseType().newInstance();
			if (this.apiResponse == null)
				throw new Exception("no such response-object.");
		} catch (Throwable e) {
			throw new InitException(e);
		}
	}

	// 1.
	public void doInit(String sign, String contentType, String messageBody) throws InitException {
		this.needCheckSign = true;
		try {
			if (contentType.equalsIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
				UrlEncodedMapper uem = new UrlEncodedMapper();
				this.apiRequest = (T) uem.readValue(messageBody, this.getApiRequestType());
				if (this.apiRequest == null)
					throw new Exception("no such request-object.");
				this.apiResponse = this.apiRequest.getApiResponseType().newInstance();
				if (this.apiResponse == null)
					throw new Exception("no such response-object.");

				this.apiRequest.initMessageBodySign(messageBody, sign);

			} else if (contentType.equalsIgnoreCase(MediaType.APPLICATION_XML_VALUE)) {
				XmlMapper mapper = new XmlMapper();
				mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
				this.apiRequest = (T) mapper.readValue(messageBody, this.getApiRequestType());
				if (this.apiRequest == null)
					throw new Exception("no such request-object.");
				this.apiResponse = this.apiRequest.getApiResponseType().newInstance();
				if (this.apiResponse == null)
					throw new Exception("no such response-object.");

				this.apiRequest.initMessageBodySign(messageBody, sign);

			} else if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
				ObjectMapper om = new ObjectMapper();
				om.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
				this.apiRequest = (T) om.readValue(messageBody, this.getApiRequestType());
				if (this.apiRequest == null)
					throw new Exception("no such request-object.");
				this.apiResponse = this.apiRequest.getApiResponseType().newInstance();
				if (this.apiResponse == null)
					throw new Exception("no such response-object.");

				this.apiRequest.initMessageBodySign(messageBody, sign);
			} else
				throw new Exception("do not support this content-type.");

		} catch (Exception e) {
			throw new InitException(e);
		}
	}

	// 2.
	public void doCheck() throws CheckException {
		if (this.needCheckSign == true)
			this.apiRequest.checkSign();
		this.apiRequest.checkSysParams();
		this.apiRequest.checkApiParams();
	}

	// 3.
	public abstract void doService() throws ServiceException;

}
