package eu.pericles.processcompiler.core;


public interface Validator {

	public static class ValidationResult {
		public static final String VALID_MESSAGE = "OK";
		private String message;
		private Exception exception;

		public boolean isValid() {
			return (message == VALID_MESSAGE);
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Exception getException() {
			return exception;
		}
		public void setException(Exception exception) {
			this.exception = exception;
		}
	}
	
	ValidationResult validate();
}
