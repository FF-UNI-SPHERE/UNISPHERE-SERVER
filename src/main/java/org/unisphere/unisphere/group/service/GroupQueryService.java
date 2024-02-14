package org.unisphere.unisphere.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRegistration;
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
	 * @return 전체 그룹 정보 (Page)
	 */
	public Page<Group> getAllGroups(Pageable pageable) {
		return groupRepository.findAll(pageable);
	}

	/**
	 * 회원이 속한 그룹 정보 조회
	 *
	 * @param member   회원
	 * @param pageable 페이징 정보
	 * @return 회원이 속한 그룹 정보 (Page)
	 */
	public Page<GroupRegistration> getMemberGroups(Member member, Pageable pageable) {
		return groupRegistrationRepository.findAllByMemberId(
				member.getId(), pageable);
	}
}
