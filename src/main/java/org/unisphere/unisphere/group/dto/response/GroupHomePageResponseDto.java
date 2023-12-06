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
@Schema(description = "단체 홈피 조회 응답")
public class GroupHomePageResponseDto {

	@Schema(description = "단체 ID", example = "1")
	private final Long groupId;

	@Schema(description = "단체 이름", example = "유니스피어")
	private final String name;

	@Schema(description = "단체 아바타 이미지 URL", example = "https://unisphere.org/avatar-images/random-string/avatar-image.png")
	private final String avatarImageUrl;

	@Schema(description = "단체 로고 이미지 URL", example = "https://unisphere.org/logo-images/random-string/logo-image.png")
	private final String logoImageUrl;

	@Schema(description = "단체 홈피 내용", example = "어쩌구 저쩌구... 단체들이 적고 싶은대로")
	private final String content;

	@Schema(description = "단체 대표 이메일", example = "example@gmail.com")
	private final String email;

	@Schema(description = "단체 사이트 URL", example = "https://unisphere.org")
	private final String groupSiteUrl;
}
