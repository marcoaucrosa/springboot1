package br.com.marco.sw.planets.api.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidatonError extends StandardError {
	
	private List<FieldMessage> errors = new ArrayList<FieldMessage>();
	
	public ValidatonError(Integer status, String msg, Long timestamp) {
		super(status, msg, timestamp);
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}
	
	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}

}
