package com.shanxin.core.exception;

public class ServiceException extends CoreException {
	private static final long serialVersionUID = 1884831021481767463L;

	public ServiceException() {
		super();
		this.code = "serviceException";
	}

	public ServiceException(String errMsg) {
		super(errMsg);
		this.code = "serviceException";
	}

	public ServiceException(Throwable ex) {
		super(ex);
		this.code = "serviceException";
	}

	// my...
	public ServiceException(String errMsg, String code) {
		super(errMsg, code);
	}

	public ServiceException(String errMsg, String code, Throwable ex) {
		super(errMsg, code, ex);
	}
}
