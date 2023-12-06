package org.unisphere.unisphere.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "단체 Preview 정보")
public class GroupPreviewDto {

	@Schema(description = "단체 ID", example = "1")
	private final Long groupId;

	@Schema(description = "단체 이름", example = "유니스피어")
	private final String name;

	@Schema(description = "단체 로고 이미지 URL", example = "https://unisphere.org/logo-images/random-string/logo-image.png")
	private final String logoImageUrl;

	@Schema(description = "단체 요약 설명", example = "유니스피어는 멋진 단체에요.")
	private final String summary;
}
