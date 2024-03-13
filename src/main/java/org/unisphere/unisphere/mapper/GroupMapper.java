package org.unisphere.unisphere.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRole;
import org.unisphere.unisphere.group.dto.GroupMemberDto;
import org.unisphere.unisphere.group.dto.GroupPreviewDto;
import org.unisphere.unisphere.group.dto.response.GroupAvatarResponseDto;
import org.unisphere.unisphere.group.dto.response.GroupHomePageResponseDto;
import org.unisphere.unisphere.group.dto.response.GroupListResponseDto;
import org.unisphere.unisphere.group.dto.response.GroupMemberListResponseDto;
import org.unisphere.unisphere.member.domain.Member;

@Mapper(componentModel = "spring")
public interface GroupMapper {

	GroupMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(GroupMapper.class);

	@Mapping(source = "group.id", target = "groupId")
	@Mapping(source = "logoImageUrl", target = "logoImageUrl")
	GroupPreviewDto toGroupPreviewDto(Group group, String logoImageUrl);

	@Mapping(source = "totalElements", target = "totalGroupCount")
	@Mapping(source = "groupPreviewDtos", target = "groups")
	GroupListResponseDto toGroupListResponseDto(List<GroupPreviewDto> groupPreviewDtos,
			long totalElements);

	@Mapping(source = "group.id", target = "groupId")
	@Mapping(source = "avatarImageUrl", target = "avatarImageUrl")
	GroupAvatarResponseDto toGroupAvatarResponseDto(Group group, String avatarImageUrl);

	@Mapping(source = "group.id", target = "groupId")
	@Mapping(source = "avatarImageUrl", target = "avatarImageUrl")
	@Mapping(source = "logoImageUrl", target = "logoImageUrl")
	GroupHomePageResponseDto toGroupHomePageResponseDto(Group group, String avatarImageUrl,
			String logoImageUrl);

	@Mapping(source = "member.id", target = "memberId")
	@Mapping(source = "avatarImageUrl", target = "avatarImageUrl")
	@Mapping(source = "role", target = "role")
	GroupMemberDto toGroupMemberDto(Member member, String avatarImageUrl, GroupRole role);

	@Mapping(source = "totalElements", target = "totalMemberCount")
	@Mapping(source = "groupMemberDtos", target = "groupMembers")
	GroupMemberListResponseDto toGroupMemberListResponseDto(List<GroupMemberDto> groupMemberDtos,
			long totalElements);
}
