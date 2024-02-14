package org.unisphere.unisphere.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.image.service.ImageService;
import org.unisphere.unisphere.log.LogLevel;
import org.unisphere.unisphere.mapper.MemberMapper;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.member.dto.request.MyAvatarUpdateRequestDto;
import org.unisphere.unisphere.member.dto.response.MyAvatarResponseDto;

@Service
@RequiredArgsConstructor
@Logging(level = LogLevel.DEBUG)
public class MemberFacadeService {

	private final MemberQueryService memberQueryService;
	private final MemberCommandService memberCommandService;
	private final MemberMapper memberMapper;
	private final ImageService imageService;

	@Transactional(readOnly = true)
	public MyAvatarResponseDto getMemberAvatar(Long memberId) {
		Member member = memberQueryService.getMember(memberId);
		return memberMapper.toMyAvatarResponseDto(member);
	}

	@Transactional
	public MyAvatarResponseDto updateMemberAvatar(Long memberId,
			MyAvatarUpdateRequestDto myAvatarUpdateRequestDto) {
		Member member = memberQueryService.getMember(memberId);
		String imageUrl = imageService.getImageUrl(
				myAvatarUpdateRequestDto.getPreSignedAvatarImageUrl());
		memberCommandService.updateAvatar(member, myAvatarUpdateRequestDto.getNickname(), imageUrl);
		return memberMapper.toMyAvatarResponseDto(member);
	}
}
