package org.unisphere.unisphere.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "내 아바타 정보 수정 요청")
public class AvatarUpdateRequestDto {

	@Schema(description = "닉네임", example = "테스트")
	private final String nickname;

	@Schema(description = "pre-signed 아바타 이미지 URL", example = "avatar-images/random-string/image.png")
	private final String preSignedAvatarImageUrl;
}
