package org.unisphere.unisphere.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "내 아바타 정보")
@FieldNameConstants
public class MyAvatarResponseDto {

	@Schema(description = "회원 ID", example = "1")
	private final Long memberId;

	@Schema(description = "닉네임", example = "테스트")
	private final String nickname;

	@Schema(description = "아바타 이미지 URL", example = "https://unisphere.org/avatar-images/random-string/image.png")
	private final String avatarImageUrl;
}
