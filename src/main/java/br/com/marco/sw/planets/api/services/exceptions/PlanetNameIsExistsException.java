package br.com.marco.sw.planets.api.services.exceptions;

public class PlanetNameIsExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String fieldName;

	public PlanetNameIsExistsException(String message) {
		super(message);
	}
	
	public PlanetNameIsExistsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PlanetNameIsExistsException(String fieldName, String message) {
		super(message);
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
