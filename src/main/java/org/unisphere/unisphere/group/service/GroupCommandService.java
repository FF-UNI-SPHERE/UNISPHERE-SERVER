package org.unisphere.unisphere.group.service;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.exception.ExceptionStatus;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRegistration;
import org.unisphere.unisphere.group.domain.GroupRole;
import org.unisphere.unisphere.group.dto.request.GroupAvatarUpdateRequestDto;
import org.unisphere.unisphere.group.dto.request.GroupCreateRequestDto;
import org.unisphere.unisphere.group.dto.request.GroupHomePageUpdateRequestDto;
import org.unisphere.unisphere.group.infrastructure.GroupRegistrationRepository;
import org.unisphere.unisphere.group.infrastructure.GroupRepository;
import org.unisphere.unisphere.image.service.ImageService;
import org.unisphere.unisphere.member.domain.Member;

@Service
@RequiredArgsConstructor
@Logging
@Transactional
public class GroupCommandService {

	private final GroupRepository groupRepository;
	private final GroupRegistrationRepository groupRegistrationRepository;
	private final ImageService imageService;


	/**
	 * 새 그룹 생성 요청
	 *
	 * @param member                그룹을 생성하는 멤버
	 * @param groupCreateRequestDto 그룹 생성 요청 DTO
	 */
	public void createGroup(Member member, GroupCreateRequestDto groupCreateRequestDto) {
		if (groupRepository.findByName(groupCreateRequestDto.getName()).isPresent()) {
			throw ExceptionStatus.ALREADY_EXIST_GROUP_NAME.toServiceException();
		}
		LocalDateTime now = LocalDateTime.now();
		String imageUrl = imageService.getImageUrl(
				groupCreateRequestDto.getPreSignedLogoImageUrl());
		Group group = Group.createGroup(
				now,
				member,
				groupCreateRequestDto.getName(),
				groupCreateRequestDto.getSummary(),
				imageUrl
		);
		groupRepository.save(group);
		GroupRegistration groupRegistration = GroupRegistration.createOwnerRegistration(
				now,
				member,
				group
		);
		groupRegistrationRepository.save(groupRegistration);
	}

	/**
	 * 그룹 아바타 수정 요청
	 *
	 * @param group                       그룹
	 * @param groupAvatarUpdateRequestDto 그룹 아바타 수정 요청 DTO
	 */
	public void updateGroupAvatar(Group group,
			GroupAvatarUpdateRequestDto groupAvatarUpdateRequestDto) {
		group.updateAvatar(
				groupAvatarUpdateRequestDto.getName(),
				groupAvatarUpdateRequestDto.getPreSignedAvatarImageUrl()
		);
		groupRepository.save(group);
	}

	/**
	 * 그룹 홈페이지 수정 요청
	 *
	 * @param group                         그룹
	 * @param groupHomePageUpdateRequestDto 그룹 홈페이지 수정 요청 DTO
	 */
	public void putGroupHomePage(Group group,
			GroupHomePageUpdateRequestDto groupHomePageUpdateRequestDto) {
		group.putHomePage(
				groupHomePageUpdateRequestDto.getPreSignedLogoImageUrl(),
				groupHomePageUpdateRequestDto.getContent(),
				groupHomePageUpdateRequestDto.getEmail(),
				groupHomePageUpdateRequestDto.getGroupSiteUrl()
		);
		groupRepository.save(group);
	}


	/**
	 * 그룹 등록 요청
	 *
	 * @param member 그룹에 등록을 요청한 멤버
	 * @param group  등록할 그룹
	 */
	public void requestRegisterGroup(Member member, Group group) {
		GroupRegistration groupRegistration = GroupRegistration.of(
				member,
				group,
				GroupRole.COMMON
		);
		groupRegistrationRepository.save(groupRegistration);
	}

	/**
	 * 그룹 등록 승인
	 *
	 * @param group        승인할 그룹
	 * @param targetMember 승인할 멤버
	 */
	public void approveGroupRegister(Group group, Member targetMember) {
		GroupRegistration groupRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), targetMember.getId())
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (groupRegistration.getRegisteredAt() != null) {
			throw ExceptionStatus.ALREADY_APPROVED_MEMBER.toServiceException();
		}
		groupRegistration.approve();
		groupRegistrationRepository.save(groupRegistration);
	}

	/**
	 * 그룹 관리자 임명
	 *
	 * @param group        그룹
	 * @param targetMember 관리자로 임명할 멤버
	 */
	public void appointGroupAdmin(Group group, Member targetMember) {
		GroupRegistration groupRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberIdAndRegisteredAtNotNull(group.getId(), targetMember.getId())
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (groupRegistration.getRole() == GroupRole.ADMIN
				|| groupRegistration.getRole() == GroupRole.OWNER) {
			throw ExceptionStatus.ALREADY_GROUP_ADMIN.toServiceException();
		}
		groupRegistration.appointAdmin();
		groupRegistrationRepository.save(groupRegistration);
	}

	/**
	 * 그룹 소유자 임명
	 *
	 * @param group        그룹
	 * @param targetMember 소유자로 임명할 멤버
	 */
	public void appointGroupOwner(Group group, Member targetMember) {
		GroupRegistration ownerRegistration = groupRegistrationRepository.findByRole(
						GroupRole.OWNER)
				.orElseThrow(ExceptionStatus.NOT_GROUP_OWNER::toServiceException);
		GroupRegistration targetRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberIdAndRegisteredAtNotNull(group.getId(), targetMember.getId())
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (targetRegistration.getRole() == GroupRole.OWNER) {
			throw ExceptionStatus.ALREADY_GROUP_OWNER.toServiceException();
		}
		ownerRegistration.appointAdmin();
		targetRegistration.appointOwner();
		groupRegistrationRepository.save(ownerRegistration);
		groupRegistrationRepository.save(targetRegistration);
		group.setOwnerMember(targetMember);
		groupRepository.save(group);
	}

	/**
	 * 그룹 삭제
	 *
	 * @param group 삭제할 그룹
	 */
	public void deleteGroup(Group group) {
		groupRepository.delete(group);
	}

	/**
	 * // @formatter:off
	 * 그룹에서 탈퇴합니다. 단체 소유자인 경우, 소유자를 관리자 중 가장 오래된 회원으로 임명합니다.
	 * 단체 관리자가 없는 경우, 일반 회원 중 가장 오래된 회원을 소유자로 임명합니다.
	 * 혼자 남은 경우, 단체 삭제합니다.
	 * // @formatter:on
	 *
	 * @param member 그룹에서 탈퇴할 멤버
	 * @param group  탈퇴할 그룹
	 */
	public void unregisterGroup(Member member, Group group) {
		GroupRegistration groupRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), member.getId())
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);

		if (groupRegistration.getRole() == GroupRole.OWNER) {
			groupRegistrationRepository
					.findByGroupIdAndRoleAndRegisteredAtNotNullOrderByRegisteredAtAsc(group.getId(),
							GroupRole.ADMIN)
					.or(() -> groupRegistrationRepository.findByGroupIdAndRoleAndRegisteredAtNotNullOrderByRegisteredAtAsc(
							group.getId(), GroupRole.COMMON))
					.ifPresentOrElse(oldestAdminOrCommonRegistration -> {
						oldestAdminOrCommonRegistration.appointOwner();
						group.setOwnerMember(oldestAdminOrCommonRegistration.getMember());
						groupRegistrationRepository.save(oldestAdminOrCommonRegistration);
						groupRegistrationRepository.delete(groupRegistration);
						group.unRegisterMember(member);
						groupRepository.save(group);
					}, () -> groupRepository.delete(group));
		} else {
			groupRegistrationRepository.delete(groupRegistration);
			group.unRegisterMember(member);
			groupRepository.save(group);
		}
	}

	/**
	 * // @formatter:off
	 * 그룹에서 멤버 추방합니다. 그룹 소유자는 자신을 제외한 모든 멤버를 추방할 수 있습니다.
	 * 그룹 관리자는 일반 멤버를 추방할 수 있습니다.
	 * 추방된 멤버는 그룹에서 탈퇴됩니다.
	 * // @formatter:on
	 *
	 * @param member       그룹 관리자 or 소유자
	 * @param group        그룹
	 * @param targetMember 추방할 멤버
	 */
	public void kickMemberFromGroup(Member member, Group group, Member targetMember) {
		GroupRegistration targetRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), targetMember.getId())
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);

		if (member.equals(group.getOwnerMember())) {
			if (targetRegistration.getRole() == GroupRole.OWNER) {
				throw ExceptionStatus.CANNOT_KICK_HIGHER_RANK_MEMBER.toServiceException();
			}
		} else {
			if (targetRegistration.getRole() == GroupRole.OWNER
					|| targetRegistration.getRole() == GroupRole.ADMIN) {
				throw ExceptionStatus.CANNOT_KICK_HIGHER_RANK_MEMBER.toServiceException();
			}
		}
		groupRegistrationRepository.delete(targetRegistration);
	}
}
