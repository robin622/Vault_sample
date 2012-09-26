package com.redhat.tools.vault.util;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 
 * @author speng@redhat.com
 * 
 */
public class BaseException extends RuntimeException {

	/** serial version uid */
	private static final long serialVersionUID = 1L;

	/** caused by */
	protected Throwable cause;

	/** parameters for more details */
	protected Object[] params;

	/**
	 * constructor.
	 * 
	 */
	public BaseException() {
		this("Error occurred.");
	}

	/**
	 * constructor.
	 * 
	 * @param cause
	 */
	public BaseException(Throwable cause) {
		this(null, null, cause);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 */
	public BaseException(String message) {
		this(message, null, null);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 * @param cause
	 */
	public BaseException(String message, Throwable cause) {
		this(message, null, cause);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 * @param params
	 */
	public BaseException(String message, Object[] params) {
		this(message, params, null);
	}

	/**
	 * constructor.
	 * 
	 * @param message
	 * @param params
	 * @param cause
	 */
	public BaseException(String message, Object[] params, Throwable cause) {
		super(message);
		this.params = params;
		this.cause = cause;
	}

	// Created to match the JDK 1.4 Throwable method.
	public Throwable initCause(Throwable cause) {
		this.cause = cause;
		return cause;
	}

	/**
	 * get message
	 */
	public String getMessage() {
		// Get this exception's message.
		String msg = super.getMessage();
		Throwable parent = this;
		Throwable child;
		// Look for nested exceptions.
		while ((child = getNestedException(parent)) != null) {
			// Get the child's message.
			String msg2 = child.getMessage();
			// If we found a message for the child exception,
			// we append it.
			if (msg2 != null) {
				if (msg != null) {
					msg += ": " + msg2;
				}
				else {
					msg = msg2;
				}
			}
			// Any BaseException nested will append its own
			// children, so we need to break out of here.
			if (child instanceof BaseException) {
				break;
			}
			parent = child;
		}
		// Return the completed message.
		return msg;
	}

	/**
     * 
     */
	public void printStackTrace() {
		this.printStackTrace(System.err);
	}

	/**
	 * 
	 */
	public void printStackTrace(PrintStream s) {
		// Print the stack trace for this exception.
		s.println(this.getMessage() + asString(params));
		super.printStackTrace(s);
		Throwable parent = this;
		Throwable child;
		// Print the stack trace for each nested exception.
		while ((child = getNestedException(parent)) != null) {
			s.print("Caused by: ");
			child.printStackTrace(s);
			if (child instanceof BaseException) {
				break;
			}
			parent = child;
		}
	}

	/**
	 * 
	 */
	public void printStackTrace(PrintWriter w) {
		// Print the stack trace for this exception.
		w.println(this.getMessage() + asString(params));
		super.printStackTrace(w);
		Throwable parent = this;
		Throwable child;
		// Print the stack trace for each nested exception.
		while ((child = getNestedException(parent)) != null) {
			w.print("Caused by: ");
			child.printStackTrace(w);
			if (child instanceof BaseException) {
				break;
			}
			parent = child;
		}
	}

	/**
     * 
     */
	public Throwable getCause() {
		return cause;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	private Throwable getNestedException(Throwable e) {
		return e == null ? null : e.getCause();
	}

	/**
	 * 
	 * @param objs
	 * @return
	 */
	private static String asString(Object[] objs) {
		if (objs == null || objs.length == 0)
			return "";
		StringBuffer buf = new StringBuffer("[");
		for (Object obj : objs) {
			buf.append(obj).append(", ");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append("]");
		return buf.toString();
	}
}