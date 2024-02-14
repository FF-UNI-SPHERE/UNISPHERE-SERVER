package org.unisphere.unisphere.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

	private Integer status;
	private String message;
	private String reason;
}
