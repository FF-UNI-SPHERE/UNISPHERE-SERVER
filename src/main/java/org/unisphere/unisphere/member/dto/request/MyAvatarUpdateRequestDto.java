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
public class MyAvatarUpdateRequestDto {

	@Schema(description = "닉네임", example = "테스트", nullable = true)
	private final String nickname;

	@Schema(description = "template 아바타 이미지 URL", example = "https://unisphere-main-image.s3.ap-northeast-2.amazonaws.com/avatar-images/random-string/avatar.png", nullable = true)
	private final String templateAvatarImageUrl;
}
