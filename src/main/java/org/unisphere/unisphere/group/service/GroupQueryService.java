package org.unisphere.unisphere.group.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.exception.ExceptionStatus;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRegistration;
import org.unisphere.unisphere.group.domain.GroupRole;
import org.unisphere.unisphere.group.infrastructure.GroupRegistrationRepository;
import org.unisphere.unisphere.group.infrastructure.GroupRepository;
import org.unisphere.unisphere.member.domain.Member;

@Service
@RequiredArgsConstructor
@Logging
public class GroupQueryService {

	private final GroupRepository groupRepository;
	private final GroupRegistrationRepository groupRegistrationRepository;

	/**
	 * 전체 그룹 정보 조회
	 *
	 * @param pageable 페이징 정보
	 * @param keyword  검색 키워드
	 * @return 전체 그룹 정보 (Page)
	 */
	public Page<Group> findAllGroups(Pageable pageable, String keyword) {
		if (keyword == null) {
			return groupRepository.findAll(pageable);
		}
		return groupRepository.findAllByNameContaining(keyword, pageable);
	}

	/**
	 * 회원이 속한 그룹 정보 조회
	 *
	 * @param memberId 회원 ID
	 * @param pageable 페이징 정보
	 * @return 회원이 속한 그룹 정보 (Page)
	 */
	public Page<GroupRegistration> findMemberGroups(Long memberId, Pageable pageable) {
		return groupRegistrationRepository.findAllByMemberId(
				memberId, pageable);
	}

	/**
	 * 그룹에 속한 회원 정보 조회
	 *
	 * @param groupId  그룹 ID
	 * @param pageable 페이징 정보
	 * @return 그룹에 속한 회원 정보 (Page)
	 */
	public Page<GroupRegistration> findGroupMembers(Long groupId, Pageable pageable) {
		return groupRegistrationRepository.findAllByGroupId(
				groupId, pageable);
	}

	/**
	 * 그룹 이름으로 Group 조회
	 *
	 * @param name 그룹 이름
	 * @return 조회된 Group 객체
	 */
	public Optional<Group> findGroupByName(String name) {
		return groupRepository.findByName(name);
	}

	/**
	 * 그룹 ID로 Group 정보를 가져온다.
	 *
	 * @param groupId 그룹 ID
	 * @return Group 객체
	 */
	public Group getGroup(Long groupId) {
		Optional<Group> group = groupRepository.findById(groupId);
		return group.orElseThrow(ExceptionStatus.NOT_FOUND_GROUP::toServiceException);
	}

	/**
	 * 그룹의 소유자 정보 조회
	 *
	 * @param groupId 그룹 ID return 그룹의 소유자 정보
	 */
	public Member findGroupOwner(Long groupId) {
		return groupRegistrationRepository.findByGroupIdAndRoleAndRegisteredAtNotNullOrderByRegisteredAtAsc(
						groupId, GroupRole.OWNER)
				.orElseThrow(ExceptionStatus.NOT_FOUND_MEMBER::toServiceException)
				.getMember();
	}

	/**
	 * 그룹의 관리자 정보 조회
	 *
	 * @param groupId 그룹 ID
	 * @return 그룹의 관리자 정보
	 */
	public List<Member> findGroupAdmins(Long groupId) {
		return groupRegistrationRepository.findAllByGroupIdAndRoleAndRegisteredAtNotNull(
						groupId, List.of(GroupRole.ADMIN, GroupRole.OWNER))
				.stream()
				.map(GroupRegistration::getMember)
				.collect(Collectors.toList());
	}

	/**
	 * 그룹의 회원 정보 조회
	 *
	 * @param groupId 그룹 ID
	 * @return 그룹의 회원 정보
	 */
	public List<Member> findGroupMembers(Long groupId) {
		return groupRegistrationRepository.findAllByGroupIdAndRoleAndRegisteredAtNotNull(
						groupId, List.of(GroupRole.COMMON, GroupRole.ADMIN, GroupRole.OWNER))
				.stream()
				.map(GroupRegistration::getMember)
				.collect(Collectors.toList());
	}

	/**
	 * 그룹 가입 신청 요청자 목록 조회
	 *
	 * @param groupId  그룹 ID
	 * @param pageable 페이징 정보
	 * @return
	 */
	public Page<GroupRegistration> findGroupRegisterRequests(Long groupId, Pageable pageable) {
		return groupRegistrationRepository.findAllByGroupIdAndRegisteredAtNull(
				groupId, pageable);
	}
}
