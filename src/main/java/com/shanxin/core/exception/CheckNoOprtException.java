package com.shanxin.core.exception;

public class CheckNoOprtException extends CheckException {
	private static final long serialVersionUID = 7146745026018770170L;

	public CheckNoOprtException() {
		super();
		this.code = "checkNoOprtException";
	}

	public CheckNoOprtException(String errMsg) {
		super(errMsg);
		this.code = "checkNoOprtException";
	}

	public CheckNoOprtException(Throwable ex) {
		super(ex);
		this.code = "checkNoOprtException";
	}

	// my...
	public CheckNoOprtException(String errMsg, String code) {
		super(errMsg, code);
	}

	public CheckNoOprtException(String errMsg, String code, Throwable ex) {
		super(errMsg, code, ex);
	}

}
