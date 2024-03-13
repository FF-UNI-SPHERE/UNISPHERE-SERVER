package org.unisphere.unisphere.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.unisphere.unisphere.group.validation.GroupHomePageUpdateValidation;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "단체 홈페이지 수정 요청")
@GroupHomePageUpdateValidation
public class GroupHomePageUpdateRequestDto {

	@Schema(description = "단체 로고 이미지 URL", example = "logo-images/random-string/logo-image.png", nullable = true)
	private final String preSignedLogoImageUrl;

	@Schema(description = "단체 소개 설명", example = "우리 단체는 어쩌구저쩌구... 이런 사람들 원함.", nullable = true)
	private final String content;

	@Schema(description = "단체 이메일 주소", example = "example@gmail.com", nullable = true)
	private final String email;

	@Schema(description = "단체 홈페이지 주소", example = "https://unisphere.org/group/1", nullable = true)
	private final String groupSiteUrl;
}
