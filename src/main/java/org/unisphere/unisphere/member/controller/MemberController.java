package org.unisphere.unisphere.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unisphere.unisphere.annotation.LoginMemberInfo;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.auth.dto.MemberSessionDto;
import org.unisphere.unisphere.member.dto.request.MyAvatarUpdateRequestDto;
import org.unisphere.unisphere.member.dto.response.MyAvatarResponseDto;
import org.unisphere.unisphere.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
@Tag(name = "회원 (Member)", description = "회원 관련 API")
public class MemberController {

	private final MemberService memberService;

	// 내 아바타 정보 조회
	// GET api/v1/members/me/avatar
	@Operation(summary = "내 아바타 정보 조회", description = "내 아바타 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/me/avatar")
	@Secured(MemberRole.S_USER)
	public MyAvatarResponseDto getMyAvatar(
			@LoginMemberInfo MemberSessionDto memberSessionDto
	) {
		log.info("Called getMyAvatar member: {}", memberSessionDto);
		return MyAvatarResponseDto.builder().build();
	}

	// 내 아바타 편집
	// PATCH api/v1/members/me/avatar
	@Operation(summary = "내 아바타 편집", description = "내 아바타 정보를 편집합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PatchMapping(value = "/me/avatar")
	@Secured(MemberRole.S_USER)
	public MyAvatarResponseDto updateAvatar(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestBody MyAvatarUpdateRequestDto myAvatarUpdateRequestDto
	) {
		log.info("Called updateAvatar member: {}, avatarUpdateRequestDto: {}", memberSessionDto,
				myAvatarUpdateRequestDto);
		return MyAvatarResponseDto.builder().build();
	}

	// 특정 회원 아바타 정보 조회
	// GET api/v1/members/{memberId}/avatar (pending)
	@Operation(summary = "특정 회원 아바타 정보 조회", description = "특정 회원 아바타 정보를 조회합니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/{memberId}/avatar")
	@Secured(MemberRole.S_USER)
	public MyAvatarResponseDto getMemberAvatar(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("memberId") Long targetMemberId
	) {
		log.info("Called getMemberAvatar member: {}, targetMemberId: {}", memberSessionDto,
				targetMemberId);
		return MyAvatarResponseDto.builder().build();
	}
}
