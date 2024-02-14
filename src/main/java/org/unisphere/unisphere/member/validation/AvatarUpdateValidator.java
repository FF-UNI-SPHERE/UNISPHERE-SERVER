package org.unisphere.unisphere.member.validation;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.unisphere.unisphere.member.dto.request.MyAvatarUpdateRequestDto;

public class AvatarUpdateValidator implements
		ConstraintValidator<AvatarUpdateValidation, MyAvatarUpdateRequestDto> {

	private static final String NICKNAME_REGEX = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]+$";
	private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);
	private static final int NICKNAME_MIN_LENGTH = 2;
	private static final int NICKNAME_MAX_LENGTH = 16;
	private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
			"jpeg", "jpg", "png");

	private boolean validateNickname(MyAvatarUpdateRequestDto value,
			ConstraintValidatorContext context) {
		if (value.getNickname() == null || value.getNickname().isEmpty()) {
			return true;
		}

		if (value.getNickname().length() < NICKNAME_MIN_LENGTH
				|| value.getNickname().length() > NICKNAME_MAX_LENGTH) {
			context.buildConstraintViolationWithTemplate("닉네임은 " + NICKNAME_MIN_LENGTH
							+ "자 이상 " + NICKNAME_MAX_LENGTH + "자 이하로 입력해주세요.")
					.addConstraintViolation();
			return false;
		}

		if (!NICKNAME_PATTERN.matcher(value.getNickname()).matches()) {
			context.buildConstraintViolationWithTemplate("닉네임은 한글, 영문, 숫자만 입력 가능합니다.")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

	private boolean validateAvatarImageUrl(MyAvatarUpdateRequestDto value,
			ConstraintValidatorContext context) {
		if (value.getPreSignedAvatarImageUrl() == null
				|| value.getPreSignedAvatarImageUrl().isEmpty()) {
			return true;
		}

		String[] split = value.getPreSignedAvatarImageUrl().split("\\.");
		String imageType = split[split.length - 1];
		if (!ALLOWED_IMAGE_TYPES.contains(imageType)) {
			context.buildConstraintViolationWithTemplate("이미지는 " + ALLOWED_IMAGE_TYPES
							+ " 형식만 지원합니다.")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid(MyAvatarUpdateRequestDto value,
			ConstraintValidatorContext context) {
		return validateNickname(value, context) && validateAvatarImageUrl(value, context);
	}
}
