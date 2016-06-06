package com.shanxin.core.api;

import com.shanxin.core.exception.CoreException;

public class ApiException extends CoreException {
	private static final long serialVersionUID = 1114959502422513754L;

	public ApiException() {
		super();
		this.code = "apiException";
	}

	public ApiException(String errMsg) {
		super(errMsg);
		this.code = "apiException";
	}

	public ApiException(Throwable ex) {
		super(ex);
		this.code = "apiException";
	}

	// my...
	public ApiException(String errMsg, String code) {
		super(errMsg, code);
	}

	public ApiException(String errMsg, String code, Throwable ex) {
		super(errMsg, code, ex);
	}
}
