package com.shanxin.core.exception;

public class InitException extends CoreException {
	private static final long serialVersionUID = -604452135069620348L;

	public InitException() {
		super();
		this.code = "initException";
	}

	public InitException(String errMsg) {
		super(errMsg);
		this.code = "initException";
	}

	public InitException(Throwable ex) {
		super(ex);
		this.code = "initException";
	}

	// my...
	public InitException(String errMsg, String code) {
		super(errMsg, code);
	}

	public InitException(String errMsg, String code, Throwable ex) {
		super(errMsg, code, ex);
	}
}
