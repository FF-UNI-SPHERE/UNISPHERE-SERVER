package org.unisphere.unisphere.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.unisphere.unisphere.group.validation.AvatarUpdateValidation;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "그룹 아바타 정보 수정 요청")
@AvatarUpdateValidation
public class GroupAvatarUpdateRequestDto {

	@Schema(description = "그룹명", example = "유니스피어", nullable = true)
	private final String name;

	@Schema(description = "pre-signed 아바타 이미지 URL", example = "avatar-images/random-string/image.png", nullable = true)
	private final String preSignedAvatarImageUrl;
}
