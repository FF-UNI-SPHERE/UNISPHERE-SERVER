package org.unisphere.unisphere.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ExceptionStatus {
	//	Member
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다"),

	//	Group
	ALREADY_EXIST_GROUP_NAME(HttpStatus.CONFLICT, "이미 존재하는 그룹 이름입니다."),

	//	Image
	NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "이미지가 존재하지 않습니다."),
	FAILED_TO_DELETE_IMAGE(HttpStatus.BAD_GATEWAY, "이미지 삭제에 실패했습니다. 다시 시도해주세요."),
	;

	private final int status;
	private final String message;
	private final String reason;

	ExceptionStatus(HttpStatus status, String message) {
		this.status = status.value();
		this.message = message;
		this.reason = status.getReasonPhrase();
	}

	public ControllerException toControllerException() {
		return new ControllerException(this);
	}

	public ServiceException toServiceException() {
		return new ServiceException(this);
	}

	public DomainException toDomainException() {
		return new DomainException(this);
	}
}
