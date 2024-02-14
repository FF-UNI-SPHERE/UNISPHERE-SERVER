package org.unisphere.unisphere.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.annotation.LoginMemberInfo;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.auth.dto.MemberSessionDto;
import org.unisphere.unisphere.member.dto.request.MyAvatarUpdateRequestDto;
import org.unisphere.unisphere.member.dto.response.MyAvatarResponseDto;
import org.unisphere.unisphere.member.service.MemberFacadeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "회원 (Member)", description = "회원 관련 API")
@Logging
public class MemberController {

	private final MemberFacadeService memberFacadeService;

	@Operation(summary = "내 아바타 정보 조회", description = "내 아바타 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/me/avatar")
	@Secured(MemberRole.S_USER)
	public MyAvatarResponseDto getMyAvatar(
			@LoginMemberInfo MemberSessionDto memberSessionDto
	) {
		return memberFacadeService.getMemberAvatar(memberSessionDto.getMemberId());
	}

	@Operation(summary = "내 아바타 편집", description = "내 아바타 정보를 편집합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PatchMapping(value = "/me/avatar")
	@Secured(MemberRole.S_USER)
	public MyAvatarResponseDto updateAvatar(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@Valid @RequestBody MyAvatarUpdateRequestDto myAvatarUpdateRequestDto
	) {
		return memberFacadeService.updateMemberAvatar(memberSessionDto.getMemberId(),
				myAvatarUpdateRequestDto);
	}

	@Operation(summary = "특정 회원 아바타 정보 조회", description = "특정 회원 아바타 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/{memberId}/avatar")
	@Secured(MemberRole.S_USER)
	public MyAvatarResponseDto getMemberAvatar(
			@PathVariable("memberId") Long targetMemberId
	) {
		return memberFacadeService.getMemberAvatar(targetMemberId);
	}
}
