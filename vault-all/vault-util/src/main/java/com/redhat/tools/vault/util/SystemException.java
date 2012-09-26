package com.redhat.tools.vault.util;

public class SystemException extends BaseException {

	/** serial version uid */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor.
	 * 
	 * @param cause
	 */
	public SystemException(Throwable cause) {
		this(null, null, cause);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 */
	public SystemException(String message) {
		this(message, null, null);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 * @param cause
	 */
	public SystemException(String message, Throwable cause) {
		this(message, null, cause);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 * @param param
	 */
	public SystemException(String message, Object param) {
		this(message, new Object[] { param }, null);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 * @param params
	 */
	public SystemException(String message, Object[] params) {
		this(message, params, null);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 * @param params
	 * @param cause
	 */
	public SystemException(String message, Object[] params, Throwable cause) {
		super(message, params, cause);
	}
}