package eu.pericles.processcompiler.core;


public interface Validator {

	public static class ValidationResult {
		public static final String VALID_MESSAGE = "OK";
		private String message;

		public boolean isValid() {
			return (message == VALID_MESSAGE);
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}

	public static class ValidationException extends Exception {
		private static final long serialVersionUID = 5035038260506935669L;

		public ValidationException(ValidationResult result) {
			super(result.getMessage());
		}
	}
	
	ValidationResult validate();
}
