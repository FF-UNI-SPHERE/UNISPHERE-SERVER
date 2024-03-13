package org.unisphere.unisphere.group.validation;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.unisphere.unisphere.group.dto.request.GroupHomePageUpdateRequestDto;

public class GroupHomePageUpdateValidator implements
		ConstraintValidator<GroupHomePageUpdateValidation, GroupHomePageUpdateRequestDto> {

	private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
			"jpeg", "jpg", "png");

	private static final int MAX_CONTENT_LENGTH = 1024;
	private static final String EMAIL_REGEX = "^[^@]+@[a-zA-Z]+\\.([a-zA-Z]+)$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	private static final int MAX_EMAIL_LENGTH = 64;
	private static final String GROUP_SITE_URL_REGEX = "^(http|https)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+(/[a-zA-Z0-9-]+)*$";
	private static final Pattern GROUP_SITE_URL_PATTERN = Pattern.compile(GROUP_SITE_URL_REGEX);
	private static final int MAX_GROUP_SITE_URL_LENGTH = 64;

	private boolean validatePreSignedLogoImageUrl(GroupHomePageUpdateRequestDto value,
			ConstraintValidatorContext context) {
		if (value.getPreSignedLogoImageUrl() == null
				|| value.getPreSignedLogoImageUrl().isEmpty()) {
			return true;
		}

		String[] split = value.getPreSignedLogoImageUrl().split("\\.");
		String imageType = split[split.length - 1];
		if (!ALLOWED_IMAGE_TYPES.contains(imageType)) {
			context.buildConstraintViolationWithTemplate("이미지는 " + ALLOWED_IMAGE_TYPES
							+ " 형식만 지원합니다.")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

	private boolean validateContent(GroupHomePageUpdateRequestDto value,
			ConstraintValidatorContext context) {
		if (value.getContent() == null || value.getContent().isEmpty()) {
			return true;
		}

		if (value.getContent().length() > MAX_CONTENT_LENGTH) {
			context.buildConstraintViolationWithTemplate("단체 소개 설명은 " + MAX_CONTENT_LENGTH
							+ "자 이하로 입력해주세요.")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

	private boolean validateEmail(GroupHomePageUpdateRequestDto value,
			ConstraintValidatorContext context) {
		if (value.getEmail() == null || value.getEmail().isEmpty()) {
			return true;
		}

		if (value.getEmail().length() > MAX_EMAIL_LENGTH) {
			context.buildConstraintViolationWithTemplate("이메일 주소는 " + MAX_EMAIL_LENGTH
							+ "자 이하로 입력해주세요.")
					.addConstraintViolation();
			return false;
		}

		if (!EMAIL_PATTERN.matcher(value.getEmail()).matches()) {
			context.buildConstraintViolationWithTemplate("이메일 형식이 올바르지 않습니다.")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

	private boolean validateGroupSiteUrl(GroupHomePageUpdateRequestDto value,
			ConstraintValidatorContext context) {
		if (value.getGroupSiteUrl() == null || value.getGroupSiteUrl().isEmpty()) {
			return true;
		}

		if (value.getGroupSiteUrl().length() > MAX_GROUP_SITE_URL_LENGTH) {
			context.buildConstraintViolationWithTemplate("홈페이지 주소는 " + MAX_GROUP_SITE_URL_LENGTH
							+ "자 이하로 입력해주세요.")
					.addConstraintViolation();
			return false;
		}

		if (!GROUP_SITE_URL_PATTERN.matcher(value.getGroupSiteUrl()).matches()) {
			context.buildConstraintViolationWithTemplate("홈페이지 주소 형식이 올바르지 않습니다.")
					.addConstraintViolation();
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid(GroupHomePageUpdateRequestDto value,
			ConstraintValidatorContext context) {
		return validatePreSignedLogoImageUrl(value, context) && validateContent(value, context)
				&& validateEmail(value, context) && validateGroupSiteUrl(value, context);
	}
}
