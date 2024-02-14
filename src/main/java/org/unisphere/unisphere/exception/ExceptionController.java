package org.unisphere.unisphere.exception;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler(ControllerException.class)
	public ResponseEntity<?> controllerExceptionHandler(ControllerException e) {
		log.info("[ControllerException] {} : {}", e.status.getReason(), e.status.getMessage());
		e.printStackTrace();
		return ResponseEntity
				.status(e.status.getStatus())
				.body(e.status);
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<?> serviceExceptionHandler(ServiceException e) {
		log.info("[ServiceException] {} : {}", e.status.getReason(), e.status.getMessage());
		e.printStackTrace();
		return ResponseEntity
				.status(e.status.getStatus())
				.body(e.status);
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<?> domainExceptionHandler(DomainException e) {
		log.warn("[DomainException] {} : {}", e.status.getReason(), e.status.getMessage());
		return ResponseEntity
				.status(e.status.getStatus())
				.body(e.status);
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBind(final BindException exception) {
		BindingResult result = exception.getBindingResult();

		String joinedMessages = result.getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(" "));

		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setReason(exception.getClass().getSimpleName());
		errorResponse.setMessage(joinedMessages);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(
			final IllegalArgumentException exception) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setReason(exception.getClass().getSimpleName());
		errorResponse.setMessage(exception.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
			final MethodArgumentTypeMismatchException exception) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setReason(exception.getClass().getSimpleName());
		errorResponse.setMessage(exception.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
			final MethodArgumentNotValidException exception) {
		final BindingResult bindingResult = exception.getBindingResult();
		String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setReason(exception.getClass().getSimpleName());
		errorResponse.setMessage(message);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
