package org.unisphere.unisphere.article.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "소식지 투고 요청")
public class ArticleSubmissionRequestDto {

	@Schema(description = "소식지 제목", example = "해안가에 밀려온 쓰레기, 누구의 잘못일까?")
	private final String title;

	@Schema(description = "소식지 내용", example = "우리 단체는 어쩌구저쩌구... 이런 사람들 원함.")
	private final String content;

	@Schema(description = "pre-signed 썸네일 이미지 URL", example = "article-images/random-string/image.png", nullable = true)
	private final String preSignedThumbnailImageUrl;
}
