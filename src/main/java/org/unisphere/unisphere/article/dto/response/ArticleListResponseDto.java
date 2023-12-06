package org.unisphere.unisphere.article.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.unisphere.unisphere.article.dto.ArticlePreviewDto;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "소식지 목록 조회 응답")
public class ArticleListResponseDto {

	@ArraySchema(arraySchema = @Schema(description = "소식지 목록", implementation = ArticlePreviewDto.class))
	private final List<ArticlePreviewDto> articles;

	@Schema(description = "전체 소식지 수", example = "142")
	private final Long totalArticleCount;
}
