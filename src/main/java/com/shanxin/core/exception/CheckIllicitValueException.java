package com.shanxin.core.exception;

public class CheckIllicitValueException extends CheckException {
	private static final long serialVersionUID = -6430538916683282808L;

	public CheckIllicitValueException() {
		super();
		this.code = "checkIllicitValueException";
	}

	public CheckIllicitValueException(String errMsg) {
		super(errMsg);
		this.code = "checkIllicitValueException";
	}

	public CheckIllicitValueException(Throwable ex) {
		super(ex);
		this.code = "checkIllicitValueException";
	}

	// my...
	public CheckIllicitValueException(String errMsg, String code) {
		super(errMsg, code);
	}

	public CheckIllicitValueException(String errMsg, String code, Throwable ex) {
		super(errMsg, code, ex);
	}
}
