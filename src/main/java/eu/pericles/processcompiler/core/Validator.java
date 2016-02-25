package eu.pericles.processcompiler.core;


public interface Validator {

	public static class ValidationResult {
		private String message;
		private boolean valid;

		public boolean isValid() {
			return valid;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}
	}

	public static class ValidationException extends Exception {
		
		private static final long serialVersionUID = 5035038260506935669L;

		public ValidationException(ValidationResult result) {
			super(result.getMessage());
		}
	}

	// Don't throw ValidateException here, invalid inputs are normal for
	// validators, even if they aren't for the application that uses them
	ValidationResult validate();
}
