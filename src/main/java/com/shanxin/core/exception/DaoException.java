package com.shanxin.core.exception;

public class DaoException extends CoreException {
	private static final long serialVersionUID = 4041518775130693534L;

	public DaoException() {
		super();
		this.code = "daoException";
	}

	public DaoException(String errMsg) {
		super(errMsg);
		this.code = "daoException";
	}

	public DaoException(Throwable ex) {
		super(ex);
		this.code = "daoException";
	}

	// my...
	public DaoException(String errMsg, String code) {
		super(errMsg, code);
	}

	public DaoException(String errMsg, String code, Throwable ex) {
		super(errMsg, code, ex);
	}
}
