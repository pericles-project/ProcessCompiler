package eu.pericles.processcompiler.web;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(getterVisibility=Visibility.NONE)
public class ApiException extends Exception {
	
	private static final long serialVersionUID = 2007265540281911347L;

	@JsonProperty
	public int status = 500;

	@JsonProperty
	public ErrorInfo error;

	public static class ErrorInfo {
		public String message = "Unknown error";

		public ErrorInfo(String msg) {
			message = msg;
		}
	}

	public ApiException() {
		this(500, "Unknown error");
	}

	public ApiException(int i, Exception e) {
		super(e.getMessage() != null ? e.getMessage() : e.getClass().getName(), e);
		status = i;
		error = new ErrorInfo(getMessage());
	}

	public ApiException(int i, String msg) {
		super(msg);
		status = i;
		error = new ErrorInfo(msg);
	}
}
