package br.com.marco.sw.planets.api.controllers.exceptions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.marco.sw.planets.api.services.exceptions.ObjectNotFoundException;
import br.com.marco.sw.planets.api.services.exceptions.PlanetNameIsExistsException;

@ControllerAdvice
public class ControllerHandlerException {
	
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		
		ValidatonError err = new ValidatonError(HttpStatus.BAD_REQUEST.value(), messageSource.getMessage("error.validation", null, Locale.ROOT), System.currentTimeMillis());
		
		for (FieldError fe: e.getBindingResult().getFieldErrors()) {
			err.addError(fe.getField(), fe.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(PlanetNameIsExistsException.class)
	public ResponseEntity<StandardError> validation(PlanetNameIsExistsException e, HttpServletRequest request) {
		
		ValidatonError err = new ValidatonError(HttpStatus.BAD_REQUEST.value(), messageSource.getMessage("error.validation", null, Locale.ROOT), System.currentTimeMillis());
		
		err.addError(e.getFieldName(), e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
}
