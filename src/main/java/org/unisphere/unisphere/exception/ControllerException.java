package org.unisphere.unisphere.exception;

import lombok.Getter;

@Getter
public class ControllerException extends RuntimeException {

	final ExceptionStatus status;

	public ControllerException(ExceptionStatus status) {
		this.status = status;
	}

	@Override
	public String getMessage() {
		return status.getMessage();
	}
}
