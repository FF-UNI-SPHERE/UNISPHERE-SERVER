package org.unisphere.unisphere.article.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.unisphere.unisphere.article.dto.ArticleAuthorDto;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "소식지 상세 조회 응답")
public class ArticleDetailResponseDto {

	@Schema(description = "소식지 ID", example = "1")
	private final Long articleId;

	@Schema(description = "소식지 제목", example = "해안가에 밀려온 쓰레기, 누구의 잘못일까?")
	private final String title;

	@Schema(description = "소식지 내용", example = "어쩌구 저쩌구... <img src='https://example.com'> 저쩌구 어쩌구...")
	private final String content;

	@Schema(description = "소식지 썸네일 이미지 URL", example = "https://example.com")
	private final String thumbnailImageUrl;

	@Schema(description = "소식지 작성일", example = "2023-11-19T15:49:32.840018")
	private final String createdAt;

	@Schema(description = "소식지 수정일", example = "2023-11-19T15:49:32.840018")
	private final String updatedAt;

	@Schema(description = "소식지 작성자 정보", implementation = ArticleAuthorDto.class)
	private final ArticleAuthorDto author;
}
