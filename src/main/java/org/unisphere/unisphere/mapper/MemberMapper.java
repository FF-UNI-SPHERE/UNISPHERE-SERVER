package org.unisphere.unisphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.member.dto.response.MyAvatarResponseDto;

@Mapper(componentModel = "spring")
public interface MemberMapper {

	MemberMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(MemberMapper.class);

	@Mapping(source = "member.id", target = "memberId")
	MyAvatarResponseDto toMyAvatarResponseDto(Member member);
}
