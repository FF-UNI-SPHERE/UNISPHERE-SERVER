package org.unisphere.unisphere.group.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "단체 아바타 정보")
public class GroupAvatarResponseDto {

	@Schema(description = "단체 ID", example = "1")
	private final Long groupId;

	@Schema(description = "닉네임", example = "테스트")
	private final String name;

	@Schema(description = "아바타 이미지 URL", example = "https://unisphere.org/avatar-images/random-string/image.png")
	private final String avatarImageUrl;
}
