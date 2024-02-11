package org.unisphere.unisphere.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.unisphere.unisphere.image.service.ImageService;
import org.unisphere.unisphere.mapper.MemberMapper;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.member.dto.request.MyAvatarUpdateRequestDto;
import org.unisphere.unisphere.member.dto.response.MyAvatarResponseDto;
import org.unisphere.unisphere.member.infrastructure.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberFacadeService {

	private final MemberRepository memberRepository;
	private final MemberMapper memberMapper;
	private final ImageService imageService;

	public MyAvatarResponseDto getMemberAvatar(Long memberId) {
		log.info("Called getMemberAvatar memberId: {}", memberId);

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new HttpClientErrorException(
						HttpStatus.NOT_FOUND,
						"memberId = " + memberId + " 에 해당하는 회원이 존재하지 않습니다."));
		return memberMapper.toMyAvatarResponseDto(member);
	}

	public MyAvatarResponseDto updateMemberAvatar(Long memberId,
			MyAvatarUpdateRequestDto myAvatarUpdateRequestDto) {
		log.info("Called updateMemberAvatar memberId: {}, myAvatarUpdateRequestDto: {}", memberId,
				myAvatarUpdateRequestDto);

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new HttpClientErrorException(
						HttpStatus.NOT_FOUND,
						"memberId = " + memberId + " 에 해당하는 회원이 존재하지 않습니다."));
		String imageUrl = imageService.getImageUrl(
				myAvatarUpdateRequestDto.getPreSignedAvatarImageUrl());
		member.updateAvatar(myAvatarUpdateRequestDto.getNickname(),
				imageUrl);
		memberRepository.save(member);
		return memberMapper.toMyAvatarResponseDto(member);
	}
}