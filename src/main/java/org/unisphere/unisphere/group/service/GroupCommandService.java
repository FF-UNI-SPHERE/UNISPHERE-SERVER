package org.unisphere.unisphere.group.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
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
import org.unisphere.unisphere.member.domain.Member;

@Service
@RequiredArgsConstructor
@Logging
@Transactional
public class GroupCommandService {

	private final GroupRepository groupRepository;
	private final GroupRegistrationRepository groupRegistrationRepository;


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
		Group group = Group.createGroup(
				now,
				member,
				groupCreateRequestDto.getName(),
				groupCreateRequestDto.getSummary(),
				groupCreateRequestDto.getPreSignedLogoImageUrl()
		);
		groupRepository.save(group);
		groupRegistrationRepository.save(
				GroupRegistration.createOwnerRegistration(
						now,
						member,
						group));
	}

	/**
	 * 그룹 아바타 수정 요청
	 *
	 * @param group                       그룹
	 * @param groupAvatarUpdateRequestDto 그룹 아바타 수정 요청 DTO
	 */
	public void updateGroupAvatar(Group group,
			GroupAvatarUpdateRequestDto groupAvatarUpdateRequestDto) {
		if (groupRepository.findByName(groupAvatarUpdateRequestDto.getName()).isPresent()) {
			throw ExceptionStatus.ALREADY_EXIST_GROUP_NAME.toServiceException();
		}
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
	public void requestRegisterGroup(Group group, Member member) {
		if (groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), member.getId()).isPresent()) {
			throw ExceptionStatus.ALREADY_GROUP_MEMBER.toServiceException();
		}
		groupRegistrationRepository.save(
				GroupRegistration.of(
						member,
						group,
						GroupRole.COMMON,
						null
				)
		);
	}

	/**
	 * 그룹 등록 승인
	 *
	 * @param group          승인할 그룹
	 * @param memberId       요청한 멤버 ID
	 * @param targetMemberId 승인할 멤버 ID
	 */
	public void approveGroupRegister(Group group, Long memberId, Long targetMemberId) {
		GroupRegistration adminRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), memberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (adminRegistration.getRole() == GroupRole.COMMON) {
			throw ExceptionStatus.NOT_GROUP_ADMIN.toServiceException();
		}
		GroupRegistration targetRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), targetMemberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (targetRegistration.getRegisteredAt() != null) {
			throw ExceptionStatus.ALREADY_APPROVED_MEMBER.toServiceException();
		}
		targetRegistration.approveRegistration(LocalDateTime.now());
		groupRegistrationRepository.save(targetRegistration);
	}

	/**
	 * 그룹 관리자 임명
	 *
	 * @param group          그룹
	 * @param memberId       요청한 멤버 ID
	 * @param targetMemberId 관리자로 임명할 멤버 ID
	 */
	public void appointGroupAdmin(Group group, Long memberId, Long targetMemberId) {
		GroupRegistration ownerRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), memberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (ownerRegistration.getRole() != GroupRole.OWNER) {
			throw ExceptionStatus.NOT_GROUP_OWNER.toServiceException();
		}
		GroupRegistration targetRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), targetMemberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (targetRegistration.isAdmin()) {
			throw ExceptionStatus.ALREADY_GROUP_ADMIN.toServiceException();
		}
		targetRegistration.appointAsAdmin();
		groupRegistrationRepository.save(targetRegistration);
	}

	/**
	 * 그룹 소유자 임명
	 *
	 * @param group          그룹
	 * @param memberId       요청한 멤버 ID
	 * @param targetMemberId 소유자로 임명할 멤버 ID
	 */
	public void appointGroupOwner(Group group, Long memberId, Long targetMemberId) {
		GroupRegistration ownerRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), memberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (ownerRegistration.getRole() != GroupRole.OWNER) {
			throw ExceptionStatus.NOT_GROUP_OWNER.toServiceException();
		}
		GroupRegistration targetRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), targetMemberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (targetRegistration.isOwner()) {
			throw ExceptionStatus.ALREADY_GROUP_OWNER.toServiceException();
		}
		ownerRegistration.appointAsAdmin();
		targetRegistration.appointAsOwner();
		groupRegistrationRepository.save(targetRegistration);
	}

	/**
	 * 그룹을 삭제합니다. 그룹 소유자만 삭제할 수 있습니다.
	 *
	 * @param group  삭제할 그룹
	 * @param member 그룹 소유자
	 */
	public void deleteGroup(Group group, Member member) {
		if (!group.isGroupOwner(member)) {
			throw ExceptionStatus.NOT_GROUP_OWNER.toServiceException();
		}
		groupRepository.delete(group);
	}

	/**
	 * // @formatter:off
	 * 그룹에서 탈퇴합니다. 그룹 소유자인 경우, 소유자를 관리자 중 가장 오래된 회원으로 임명합니다.
	 * 그룹 관리자가 없는 경우, 일반 회원 중 가장 오래된 회원을 소유자로 임명합니다.
	 * 혼자 남은 경우, 그룹을 삭제합니다.
	 * // @formatter:on
	 *
	 * @param group  탈퇴할 그룹
	 * @param memberId 탈퇴 요청한 멤버 ID
	 */
	public void unregisterGroup(Group group, Long memberId) {
		GroupRegistration groupRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), memberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);

		if (groupRegistration.getRole() == GroupRole.OWNER) {
			Optional<GroupRegistration> newOwnerCandidate = group.getGroupRegistrations().stream()
					.filter(registration -> !registration.equals(groupRegistration))
					.min(Comparator.comparing(GroupRegistration::getRegisteredAt));

			if (newOwnerCandidate.isPresent()) {
				GroupRegistration newOwnerRegistration = newOwnerCandidate.get();
				newOwnerRegistration.appointAsOwner();
				groupRegistrationRepository.save(newOwnerRegistration);
				groupRegistrationRepository.delete(groupRegistration);
			} else {
				groupRepository.delete(group);
				return;
			}
		} else {
			groupRegistrationRepository.delete(groupRegistration);
		}
		groupRepository.save(group);
	}

	/**
	 * // @formatter:off
	 * 그룹에서 멤버 추방합니다. 그룹 소유자는 자신을 제외한 모든 멤버를 추방할 수 있습니다.
	 * 그룹 관리자는 일반 멤버를 추방할 수 있습니다.
	 * 추방된 멤버는 그룹에서 탈퇴됩니다.
	 * // @formatter:on
	 *
	 * @param group        그룹
	 * @param memberId     요청한 멤버 ID
	 * @param targetMemberId 추방할 멤버 ID
	 */
	public void kickMemberFromGroup(Group group, Long memberId, Long targetMemberId) {
		GroupRegistration adminRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), memberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (adminRegistration.getRole() == GroupRole.COMMON) {
			throw ExceptionStatus.NOT_GROUP_ADMIN.toServiceException();
		}

		GroupRegistration targetRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), targetMemberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);

		if (adminRegistration.getRole() == GroupRole.OWNER) {
			if (targetRegistration.isOwner()) {
				throw ExceptionStatus.CANNOT_KICK_HIGHER_RANK_MEMBER.toServiceException();
			}
		} else {
			if (targetRegistration.isAdmin() || targetRegistration.isOwner()) {
				throw ExceptionStatus.CANNOT_KICK_HIGHER_RANK_MEMBER.toServiceException();
			}
		}
		groupRegistrationRepository.delete(targetRegistration);
	}

	/**
	 * 그룹 등록을 거절합니다. 그룹 관리자만 거절할 수 있습니다.
	 *
	 * @param group          그룹
	 * @param memberId       요청한 멤버 ID
	 * @param targetMemberId 거절할 멤버 ID
	 */
	public void rejectGroupRegister(Group group, Long memberId, Long targetMemberId) {
		GroupRegistration adminRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberId(group.getId(), memberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_MEMBER::toServiceException);
		if (adminRegistration.getRole() == GroupRole.COMMON) {
			throw ExceptionStatus.NOT_GROUP_ADMIN.toServiceException();
		}
		GroupRegistration targetRegistration = groupRegistrationRepository
				.findByGroupIdAndMemberIdAndRegisteredAtNull(group.getId(), targetMemberId)
				.orElseThrow(ExceptionStatus.NOT_GROUP_REQUEST_MEMBER::toServiceException);
		groupRegistrationRepository.delete(targetRegistration);
	}
}
