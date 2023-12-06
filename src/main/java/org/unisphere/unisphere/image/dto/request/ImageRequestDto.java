package org.unisphere.unisphere.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@Schema(description = "presigned-url 발급을 위한 정보 요청")
public class ImageRequestDto {

	@Schema(description = "s3에 저장될 파일 경로 (ObjectKey)", example = "logo-images/random-string/logo.png")
	private String imageUrl;

	@Builder
	public ImageRequestDto(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
