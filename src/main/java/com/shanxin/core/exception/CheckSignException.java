package com.shanxin.core.exception;

public class CheckSignException extends CheckException {
	private static final long serialVersionUID = 8667638457262459160L;

	public CheckSignException() {
		super();
		this.code = "checkSignException";
	}

	public CheckSignException(String errMsg) {
		super(errMsg);
		this.code = "checkSignException";
	}

	public CheckSignException(Throwable ex) {
		super(ex);
		this.code = "checkSignException";
	}

	// my...
	public CheckSignException(String errMsg, String code) {
		super(errMsg, code);
	}

	public CheckSignException(String errMsg, String code, Throwable ex) {
		super(errMsg, code, ex);
	}
}
