package org.unisphere.unisphere.group.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.exception.ExceptionStatus;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRegistration;
import org.unisphere.unisphere.group.dto.GroupMemberDto;
import org.unisphere.unisphere.group.dto.GroupPreviewDto;
import org.unisphere.unisphere.group.dto.request.GroupAvatarUpdateRequestDto;
import org.unisphere.unisphere.group.dto.request.GroupCreateRequestDto;
import org.unisphere.unisphere.group.dto.request.GroupHomePageUpdateRequestDto;
import org.unisphere.unisphere.group.dto.response.GroupAvatarResponseDto;
import org.unisphere.unisphere.group.dto.response.GroupHomePageResponseDto;
import org.unisphere.unisphere.group.dto.response.GroupListResponseDto;
import org.unisphere.unisphere.group.dto.response.GroupMemberListResponseDto;
import org.unisphere.unisphere.image.service.ImageService;
import org.unisphere.unisphere.log.LogLevel;
import org.unisphere.unisphere.mapper.GroupMapper;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.member.service.MemberQueryService;

@Service
@RequiredArgsConstructor
@Logging(level = LogLevel.DEBUG)
public class GroupFacadeService {

	private final GroupQueryService groupQueryService;
	private final GroupCommandService groupCommandService;
	private final MemberQueryService memberQueryService;
	private final GroupMapper groupMapper;
	private final ImageService imageService;

	@Transactional(readOnly = true)
	public GroupListResponseDto getAllGroups(Pageable pageable) {
		Page<Group> groups = groupQueryService.findAllGroups(pageable);
		List<GroupPreviewDto> groupPreviewDtos = groups.stream()
				.sorted(Comparator.comparing(Group::getName))
				.map(group -> groupMapper.toGroupPreviewDto(group,
						imageService.findImageUrl(group.getLogoImageUrl())))
				.collect(Collectors.toList());
		return groupMapper.toGroupListResponseDto(groupPreviewDtos, groups.getTotalElements());
	}

	@Transactional(readOnly = true)
	public GroupListResponseDto getMyGroups(Long memberId, Pageable pageable) {
		Page<GroupRegistration> groupRegistrations = groupQueryService.findMemberGroups(memberId,
				pageable);
		List<GroupPreviewDto> groupPreviewDtos = groupRegistrations.stream()
				.sorted(Comparator.comparing(
						groupRegistration -> groupRegistration.getGroup().getName()))
				.map(groupRegistration -> groupMapper.toGroupPreviewDto(
						groupRegistration.getGroup(), imageService.findImageUrl(
								groupRegistration.getGroup().getLogoImageUrl())))
				.collect(Collectors.toList());
		return groupMapper.toGroupListResponseDto(groupPreviewDtos,
				groupRegistrations.getTotalElements());
	}

	@Transactional(readOnly = true)
	public GroupListResponseDto getMemberGroups(Long memberId, Pageable pageable) {
		Page<GroupRegistration> groupRegistrations = groupQueryService.findMemberGroups(memberId,
				pageable);
		List<GroupPreviewDto> groupPreviewDtos = groupRegistrations.stream()
				.sorted(Comparator.comparing(
						groupRegistration -> groupRegistration.getGroup().getName()))
				.map(groupRegistration -> groupMapper.toGroupPreviewDto(
						groupRegistration.getGroup(), imageService.findImageUrl(
								groupRegistration.getGroup().getLogoImageUrl())))
				.collect(Collectors.toList());
		return groupMapper.toGroupListResponseDto(groupPreviewDtos,
				groupRegistrations.getTotalElements());
	}

	@Transactional(readOnly = true)
	public GroupMemberListResponseDto getGroupMembers(Long groupId, Pageable pageable) {
		Page<GroupRegistration> groupRegistrations = groupQueryService.findGroupMembers(groupId,
				pageable);
		List<GroupMemberDto> groupMemberDtos = groupRegistrations.stream()
				.sorted(Comparator.comparing(
						groupRegistration -> groupRegistration.getMember().getNickname()))
				.map(groupRegistration -> groupMapper.toGroupMemberDto(
						groupRegistration.getMember(),
						imageService.findImageUrl(
								groupRegistration.getMember().getAvatarImageUrl()),
						groupRegistration.getRole()
				))
				.collect(Collectors.toList());
		return groupMapper.toGroupMemberListResponseDto(groupMemberDtos,
				groupRegistrations.getTotalElements());
	}

	@Transactional(readOnly = true)
	public GroupAvatarResponseDto getGroupAvatar(Long groupId) {
		Group group = groupQueryService.getGroup(groupId);
		String imageUrl = imageService.findImageUrl(
				group.getAvatarImageUrl());
		return groupMapper.toGroupAvatarResponseDto(group, imageUrl);
	}

	@Transactional(readOnly = true)
	public GroupHomePageResponseDto getGroupHomePage(Long groupId) {
		Group group = groupQueryService.getGroup(groupId);
		return groupMapper.toGroupHomePageResponseDto(group,
				imageService.findImageUrl(
						group.getAvatarImageUrl()),
				imageService.findImageUrl(
						group.getLogoImageUrl()));
	}

	@Transactional
	public void createGroup(Long memberId, GroupCreateRequestDto groupCreateRequestDto) {
		Member member = memberQueryService.getMember(memberId);
		if (groupQueryService.findGroupByName(groupCreateRequestDto.getName()).isPresent()) {
			throw ExceptionStatus.ALREADY_EXIST_GROUP_NAME.toServiceException();
		}
		groupCommandService.createGroup(member, groupCreateRequestDto);
	}

	@Transactional
	public GroupAvatarResponseDto updateGroupAvatar(Long memberId, Long groupId,
			GroupAvatarUpdateRequestDto groupAvatarUpdateRequestDto) {
		Member member = memberQueryService.getMember(memberId);
		Group group = groupQueryService.getGroup(groupId);
		if (!group.isGroupAdmin(member)) {
			throw ExceptionStatus.NOT_GROUP_ADMIN.toServiceException();
		}
		String imageUrl = imageService.getImageUrl(
				groupAvatarUpdateRequestDto.getPreSignedAvatarImageUrl());
		if (imageUrl == null) {
			imageUrl = imageService.findImageUrl(group.getAvatarImageUrl());
		}
		groupCommandService.updateGroupAvatar(group, groupAvatarUpdateRequestDto);
		return groupMapper.toGroupAvatarResponseDto(group, imageUrl);
	}

	@Transactional
	public GroupHomePageResponseDto putGroupHomePage(Long memberId, Long groupId,
			GroupHomePageUpdateRequestDto groupHomePageUpdateRequestDto) {
		Member member = memberQueryService.getMember(memberId);
		Group group = groupQueryService.getGroup(groupId);
		if (!group.isGroupAdmin(member)) {
			throw ExceptionStatus.NOT_GROUP_ADMIN.toServiceException();
		}
		String logoImageUrl = imageService.getImageUrl(
				groupHomePageUpdateRequestDto.getPreSignedLogoImageUrl());
		String avatarImageUrl = imageService.findImageUrl(
				group.getAvatarImageUrl());
		groupCommandService.putGroupHomePage(group, groupHomePageUpdateRequestDto);
		return groupMapper.toGroupHomePageResponseDto(group, avatarImageUrl, logoImageUrl);
	}

	@Transactional
	public void requestRegisterGroup(Long memberId, Long groupId) {
		Member member = memberQueryService.getMember(memberId);
		Group group = groupQueryService.getGroup(groupId);
		if (group.isGroupMember(member)) {
			throw ExceptionStatus.ALREADY_GROUP_MEMBER.toServiceException();
		}
		groupCommandService.requestRegisterGroup(member, group);
	}

	@Transactional
	public void approveGroupRegister(Long memberId, Long groupId, Long targetMemberId) {
		Member member = memberQueryService.getMember(memberId);
		Group group = groupQueryService.getGroup(groupId);
		if (!group.isGroupAdmin(member)) {
			throw ExceptionStatus.NOT_GROUP_ADMIN.toServiceException();
		}
		Member targetMember = memberQueryService.getMember(targetMemberId);
		groupCommandService.approveGroupRegister(group, targetMember);
	}

	@Transactional
	public void appointGroupAdmin(Long memberId, Long groupId, Long targetMemberId) {
		Member member = memberQueryService.getMember(memberId);
		Group group = groupQueryService.getGroup(groupId);
		if (!group.isOwner(member)) {
			throw ExceptionStatus.NOT_GROUP_OWNER.toServiceException();
		}
		Member targetMember = memberQueryService.getMember(targetMemberId);
		groupCommandService.appointGroupAdmin(group, targetMember);
	}

	@Transactional
	public void appointGroupOwner(Long memberId, Long groupId, Long targetMemberId) {
		Member member = memberQueryService.getMember(memberId);
		Group group = groupQueryService.getGroup(groupId);
		if (!group.isOwner(member)) {
			throw ExceptionStatus.NOT_GROUP_OWNER.toServiceException();
		}
		Member targetMember = memberQueryService.getMember(targetMemberId);
		groupCommandService.appointGroupOwner(group, targetMember);
	}
}
