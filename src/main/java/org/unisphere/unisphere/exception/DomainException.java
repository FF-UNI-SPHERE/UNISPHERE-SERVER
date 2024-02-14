package org.unisphere.unisphere.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

	final ExceptionStatus status;

	public DomainException(ExceptionStatus status) {
		this.status = status;
	}

	@Override
	public String getMessage() {
		return status.getMessage();
	}
}
