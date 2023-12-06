package org.unisphere.unisphere.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.unisphere.unisphere.article.domain.AuthorType;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "소식지 작성자 정보")
public class ArticleAuthorDto {

	@Schema(description = "작성자 ID (회원 ID or 그룹 ID)", example = "1")
	private final Long id;

	@Schema(description = "작성자 이름", example = "sichoi")
	private final String name;

	@Schema(description = "작성자 타입", enumAsRef = true, example = "MEMBER")
	private final AuthorType type;
}
