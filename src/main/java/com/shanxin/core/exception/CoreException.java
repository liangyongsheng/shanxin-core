package com.shanxin.core.exception;

public class CoreException extends Exception {
	private static final long serialVersionUID = 2387471240947216134L;
	protected String code;

	public CoreException() {
		super();
		this.code = "coreException";
	}

	public CoreException(String errMsg) {
		super(errMsg);
		this.code = "coreException";
	}

	public CoreException(Throwable ex) {
		super(ex);
		this.code = "coreException";
	}

	// my...
	public CoreException(String errMsg, String code) {
		super(errMsg);
		this.code = code;
	}

	public CoreException(String errMsg, String code, Throwable ex) {
		super(errMsg, ex);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
