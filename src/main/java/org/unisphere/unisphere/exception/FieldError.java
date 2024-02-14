package org.unisphere.unisphere.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldError {

	private String field;
	private String errorCode;

}
