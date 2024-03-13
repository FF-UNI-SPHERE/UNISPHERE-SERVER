package org.unisphere.unisphere.group.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.unisphere.unisphere.group.dto.GroupPreviewDto;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "단체 목록 조회 응답")
@FieldNameConstants
public class GroupListResponseDto {

	@ArraySchema(schema = @Schema(implementation = GroupPreviewDto.class))
	private final List<GroupPreviewDto> groups;

	@Schema(description = "전체 단체 개수", example = "21")
	private final long totalGroupCount;
}
