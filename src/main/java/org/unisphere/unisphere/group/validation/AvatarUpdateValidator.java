package org.unisphere.unisphere.group.validation;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.unisphere.unisphere.group.dto.request.GroupAvatarUpdateRequestDto;

public class AvatarUpdateValidator implements
		ConstraintValidator<AvatarUpdateValidation, GroupAvatarUpdateRequestDto> {

	private static final String GROUP_NAME_REGEX = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]+$";
	private static final Pattern GROUP_NAME_PATTERN = Pattern.compile(GROUP_NAME_REGEX);
	private static final int GROUP_NAME_MIN_LENGTH = 2;
	private static final int GROUP_NAME_MAX_LENGTH = 16;
	private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
			"jpeg", "jpg", "png");

	private boolean validateGroupName(GroupAvatarUpdateRequestDto value,
			ConstraintValidatorContext context) {
		if (value.getName() == null || value.getName().isEmpty()) {
			return true;
		}

		if (value.getName().length() < GROUP_NAME_MIN_LENGTH
				|| value.getName().length() > GROUP_NAME_MAX_LENGTH) {
			context.buildConstraintViolationWithTemplate("그룹명은 " + GROUP_NAME_MIN_LENGTH
							+ "자 이상 " + GROUP_NAME_MAX_LENGTH + "자 이하로 입력해주세요.")
					.addConstraintViolation();
			return false;
		}

		if (!GROUP_NAME_PATTERN.matcher(value.getName()).matches()) {
			context.buildConstraintViolationWithTemplate("그룹명은 한글, 영문, 숫자만 입력 가능합니다.")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

	private boolean validateAvatarImageUrl(GroupAvatarUpdateRequestDto value,
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
	public boolean isValid(GroupAvatarUpdateRequestDto value, ConstraintValidatorContext context) {
		return validateGroupName(value, context) && validateAvatarImageUrl(value, context);
	}
}
