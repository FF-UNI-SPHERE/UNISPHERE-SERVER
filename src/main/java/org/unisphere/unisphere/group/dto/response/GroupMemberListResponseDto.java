package org.unisphere.unisphere.group.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.unisphere.unisphere.group.dto.GroupMemberDto;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Schema(description = "단체에 속한 회원 목록 정보")
public class GroupMemberListResponseDto {

	@Schema(description = "단체에 속한 회원 목록", implementation = GroupMemberDto.class)
	private final List<GroupMemberDto> groupMembers;

	@Schema(description = "전체 회원 수", example = "100")
	private final int totalMemberCount;
}
