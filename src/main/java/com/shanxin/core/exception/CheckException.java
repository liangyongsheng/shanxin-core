package com.shanxin.core.exception;

public class CheckException extends CoreException {
	private static final long serialVersionUID = 6224458067278021776L;

	public CheckException() {
		super();
		this.code = "checkException";
	}

	public CheckException(String errMsg) {
		super(errMsg);
		this.code = "checkException";
	}

	public CheckException(Throwable ex) {
		super(ex);
		this.code = "checkException";
	}

	// my...
	public CheckException(String errMsg, String code) {
		super(errMsg, code);
	}

	public CheckException(String errMsg, String code, Throwable ex) {
		super(errMsg, code, ex);
	}

}
