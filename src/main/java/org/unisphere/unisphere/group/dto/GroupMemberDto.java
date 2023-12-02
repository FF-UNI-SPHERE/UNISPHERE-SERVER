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
@Schema(description = "단체에 속한 회원 정보")
public class GroupMemberDto {

	@Schema(description = "회원 ID", example = "1")
	private final Long memberId;

	@Schema(description = "회원 닉네임", example = "테스트")
	private final String nickname;

	@Schema(description = "회원 아바타 이미지 URL", example = "https://unisphere.org/member-images/random-string/avatar-image.png")
	private final String avatarImageUrl;
}
