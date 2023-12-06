package org.unisphere.unisphere.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "소식지 Preview 정보")
public class ArticlePreviewDto {

	@Schema(description = "소식지 ID", example = "1")
	private final Long articleId;

	@Schema(description = "소식지 제목", example = "해안가에 밀려온 쓰레기, 누구의 잘못일까?")
	private final String title;

	@Schema(description = "소식지 썸네일 이미지 URL", example = "https://unisphere.org/article-images/random-string/thumnail-image.png")
	private final String thumbnailImageUrl;

	@Schema(description = "소식지 작성일", example = "2023-11-19T15:49:32.840018")
	private final String createdAt;

	@Schema(description = "소식지 수정일", example = "2023-11-19T15:49:32.840018")
	private final String updatedAt;

	@Schema(description = "소식지 작성자", implementation = ArticleAuthorDto.class)
	private final ArticleAuthorDto author;
}
