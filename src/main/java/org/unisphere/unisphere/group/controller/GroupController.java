package org.unisphere.unisphere.group.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.annotation.LoginMemberInfo;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.auth.dto.MemberSessionDto;
import org.unisphere.unisphere.group.dto.response.GroupHomePageResponseDto;
import org.unisphere.unisphere.group.dto.request.GroupAvatarUpdateRequestDto;
import org.unisphere.unisphere.group.dto.request.GroupHomePageUpdateRequestDto;
import org.unisphere.unisphere.group.dto.response.GroupAvatarResponseDto;
import org.unisphere.unisphere.group.dto.request.GroupCreateRequestDto;
import org.unisphere.unisphere.group.dto.response.GroupListResponseDto;
import org.unisphere.unisphere.group.dto.response.GroupMemberListResponseDto;
import org.unisphere.unisphere.group.service.GroupFacadeService;

@RestController
@RequiredArgsConstructor
@Logging
@RequestMapping("/api/v1/groups")
@Tag(name = "단체 (Group)", description = "단체 관련 API")
public class GroupController {

	private final GroupFacadeService groupFacadeService;

	@Operation(summary = "전체 단체 목록 조회", description = "전체 단체 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/all")
	@Secured(MemberRole.S_USER)
	public GroupListResponseDto getAllGroups(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String keyword
	) {
		return groupFacadeService.getAllGroups(PageRequest.of(page, size), keyword);
	}

	@Operation(summary = "내가 속한 단체 목록 조회", description = "내가 속한 단체 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/members/me")
	@Secured(MemberRole.S_USER)
	public GroupListResponseDto getMyGroups(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return groupFacadeService.getMyGroups(memberSessionDto.getMemberId(),
				PageRequest.of(page, size));
	}

	@Operation(summary = "특정 회원이 속한 단체 목록 조회", description = "특정 회원이 속한 단체 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/members/{memberId}")
	@Secured(MemberRole.S_USER)
	public GroupListResponseDto getMemberGroups(
			@PathVariable("memberId") Long targetMemberId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return groupFacadeService.getMemberGroups(targetMemberId, PageRequest.of(page, size));
	}

	@Operation(summary = "특정 단체 아바타 조회", description = "특정 단체 아바타를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/{groupId}/avatar")
	@Secured(MemberRole.S_USER)
	public GroupAvatarResponseDto getGroupAvatar(
			@PathVariable("groupId") Long groupId
	) {
		return groupFacadeService.getGroupAvatar(groupId);
	}

	@Operation(summary = "특정 단체 아바타 편집", description = "특정 단체 아바타를 편집합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PatchMapping(value = "/{groupId}/avatar")
	@Secured(MemberRole.S_USER)
	public GroupAvatarResponseDto updateGroupAvatar(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@Valid @RequestBody GroupAvatarUpdateRequestDto groupAvatarUpdateRequestDto
	) {
		return groupFacadeService.updateGroupAvatar(memberSessionDto.getMemberId(), groupId,
				groupAvatarUpdateRequestDto);
	}

	@Operation(summary = "특정 단체의 홈피 정보 조회", description = "특정 단체의 홈피 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/{groupId}/home-page")
	@Secured(MemberRole.S_USER)
	public GroupHomePageResponseDto getGroupHomePage(
			@PathVariable("groupId") Long groupId
	) {
		return groupFacadeService.getGroupHomePage(groupId);
	}

	@Operation(summary = "특정 단체의 홈피 정보 편집", description = "특정 단체의 홈피 정보를 편집합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PutMapping(value = "/{groupId}/home-page")
	@Secured(MemberRole.S_USER)
	public GroupHomePageResponseDto putGroupHomePage(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@Valid @RequestBody GroupHomePageUpdateRequestDto groupHomePageUpdateRequestDto
	) {
		return groupFacadeService.putGroupHomePage(memberSessionDto.getMemberId(), groupId,
				groupHomePageUpdateRequestDto);
	}

	@Operation(summary = "특정 단체에 속한 회원 목록 조회", description = "특정 단체의 속한 회원 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/{groupId}/members")
	@Secured(MemberRole.S_USER)
	public GroupMemberListResponseDto getGroupMembers(
			@PathVariable("groupId") Long groupId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return groupFacadeService.getGroupMembers(groupId, PageRequest.of(page, size));
	}

	@Operation(summary = "단체 생성 요청", description = "단체를 생성을 요청합니다. 유니스피어 관리자의 승인이 필요합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured(MemberRole.S_USER)
	public void createGroup(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@Valid @RequestBody GroupCreateRequestDto groupCreateRequestDto
	) {
		groupFacadeService.createGroup(memberSessionDto.getMemberId(), groupCreateRequestDto);
	}

	// 단체 생성 승인
	// PATCH /api/v1/groups/{groupId}/accept (pending)
	@Operation(summary = "단체 생성 승인", description = "단체 생성을 승인합니다. 유니스피어 관리자만 호출 가능합니다. (pending)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PatchMapping(value = "/{groupId}/accept")
	@Secured(MemberRole.S_ADMIN)
	public void acceptGroupCreation(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId
	) {
	}

	@Operation(summary = "단체 가입 요청", description = "특정 단체에 가입 요청합니다. 이미 가입한 회원은 요청할 수 없습니다. 단체 관리자의 승인이 필요합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@PostMapping(value = "/{groupId}/register")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured(MemberRole.S_USER)
	public void requestRegisterGroup(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId
	) {
		groupFacadeService.requestRegisterGroup(memberSessionDto.getMemberId(), groupId);
	}

	@Operation(summary = "단체 가입 요청한 회원 목록 조회", description = "특정 단체에 가입 요청한 회원 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@GetMapping(value = "/{groupId}/register")
	@Secured(MemberRole.S_USER)
	public GroupMemberListResponseDto getGroupRegisterRequests(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return groupFacadeService.getGroupRegisterRequests(memberSessionDto.getMemberId(), groupId,
				PageRequest.of(page, size));
	}

	@Operation(summary = "단체 가입 승인", description = "특정 회원의 단체 가입을 승인합니다. 단체 관리자만 호출할 수 있습니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@PatchMapping(value = "/{groupId}/members/{memberId}/register/approve")
	@Secured(MemberRole.S_USER)
	public void approveGroupRegister(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@PathVariable("memberId") Long targetMemberId
	) {
		groupFacadeService.approveGroupRegister(memberSessionDto.getMemberId(), groupId,
				targetMemberId);
	}

	@Operation(summary = "단체 가입 거절", description = "특정 회원의 단체 가입을 거절합니다. 단체 관리자만 호출할 수 있습니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@DeleteMapping(value = "/{groupId}/members/{memberId}/register/reject")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Secured(MemberRole.S_USER)
	public void rejectGroupRegister(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@PathVariable("memberId") Long targetMemberId
	) {
		groupFacadeService.rejectGroupRegister(memberSessionDto.getMemberId(), groupId,
				targetMemberId);
	}

	@Operation(summary = "단체 관리자 임명", description = "특정 회원을 단체 관리자로 임명합니다. 단체 소유자만 호출할 수 있습니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@PatchMapping(value = "/{groupId}/members/{memberId}/admin/appoint")
	@Secured(MemberRole.S_USER)
	public void appointGroupAdmin(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@PathVariable("memberId") Long targetMemberId
	) {
		groupFacadeService.appointGroupAdmin(memberSessionDto.getMemberId(), groupId,
				targetMemberId);
	}

	@Operation(
			summary = "단체 소유자 위임",
			description = "특정 단체의 소유자를 변경합니다. 단체 소유자만 호출할 수 있습니다.",
			deprecated = true
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@PatchMapping(value = "/{groupId}/members/{memberId}/owner/appoint")
	@Secured(MemberRole.S_USER)
	public void appointGroupOwner(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@PathVariable("memberId") Long targetMemberId
	) {
		groupFacadeService.appointGroupOwner(memberSessionDto.getMemberId(), groupId,
				targetMemberId);
	}

	@Operation(
			summary = "단체 삭제",
			description = "특정 단체를 삭제합니다. 단체 소유자만 호출할 수 있습니다."
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "no content")
	})
	@DeleteMapping(value = "/{groupId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Secured(MemberRole.S_USER)
	public void deleteGroup(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId
	) {
		groupFacadeService.deleteGroup(memberSessionDto.getMemberId(), groupId);
	}

	@Operation(
			summary = "단체 탈퇴",
			description = "특정 단체를 떠납니다. 단체 소유자가 요청하면 소유자를 다른 단체 관리자나 단체 회원에게 위임하고, 혼자 남아있는 상태에서 요청한 경우에는 단체가 삭제됩니다."
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "no content")
	})
	@DeleteMapping(value = "/{groupId}/unregister")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Secured(MemberRole.S_USER)
	public void unregisterGroup(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId
	) {
		groupFacadeService.unregisterGroup(memberSessionDto.getMemberId(), groupId);
	}

	// 특정 회원을 단체에 초대
	// POST /api/v1/groups/{groupId}/members/{memberId}/invite (pending)

	// 초대 코드로 단체 가입
	// POST /api/v1/groups/{groupId}/members/{memberId}/accept?code={code} (pending)

	@Operation(
			summary = "단체에서 회원 추방",
			description = "특정 단체에서 특정 회원을 추방합니다. 단체 관리자 이상의 등급만 호출할 수 있으며 자신보다 등급이 높거나 같은 대상에게는 호출할 수 없습니다."
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "no content")
	})
	@DeleteMapping(value = "/{groupId}/members/{memberId}/kick")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Secured(MemberRole.S_USER)
	public void kickMemberFromGroup(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@PathVariable("memberId") Long targetMemberId
	) {
		groupFacadeService.kickMemberFromGroup(memberSessionDto.getMemberId(), groupId,
				targetMemberId);
	}
}
