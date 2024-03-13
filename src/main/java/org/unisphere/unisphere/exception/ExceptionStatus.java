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
	NOT_FOUND_GROUP(HttpStatus.NOT_FOUND, "그룹이 존재하지 않습니다."),
	NOT_GROUP_OWNER(HttpStatus.FORBIDDEN, "그룹의 소유자가 아닙니다."),
	NOT_GROUP_ADMIN(HttpStatus.FORBIDDEN, "그룹의 관리자가 아닙니다."),
	ALREADY_GROUP_MEMBER(HttpStatus.CONFLICT, "이미 그룹에 가입되어 있거나 가입 요청이 처리 중입니다."),
	ALREADY_APPROVED_MEMBER(HttpStatus.CONFLICT, "이미 가입 승인된 회원입니다."),
	NOT_GROUP_MEMBER(HttpStatus.NOT_FOUND, "그룹에 가입되어 있지 않거나 가입 요청이 처리 중입니다."),
	NOT_GROUP_REQUEST_MEMBER(HttpStatus.NOT_FOUND, "가입 요청 대기중인 회원이 아닙니다."),
	ALREADY_GROUP_ADMIN(HttpStatus.CONFLICT, "이미 그룹 관리자입니다."),
	ALREADY_GROUP_OWNER(HttpStatus.CONFLICT, "이미 그룹 소유자입니다."),
	CANNOT_KICK_HIGHER_RANK_MEMBER(HttpStatus.FORBIDDEN, "자신보다 높거나 같은 등급의 그룹 회원을 추방할 수 없습니다."),

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
