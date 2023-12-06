package org.unisphere.unisphere.group.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "그룹 생성 요청")
public class GroupCreateRequestDto {

	@Schema(description = "그룹명", example = "유니스피어")
	private final String name;

	@Schema(description = "pre-signed 그룹 로고 이미지 URL", example = "logo-images/random-string/logo-image.png", nullable = true)
	private final String preSignedLogoImageUrl;

	@Schema(description = "그룹 요약 설명", example = "유니스피어는 멋진 단체에요.", nullable = true)
	private final String summary;
}
