package org.unisphere.unisphere.group.service;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.exception.ExceptionStatus;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRegistration;
import org.unisphere.unisphere.group.dto.request.GroupCreateRequestDto;
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
		Group group = Group.createGroup(
				now,
				member,
				groupCreateRequestDto.getName(),
				groupCreateRequestDto.getSummary(),
				imageService.getImageUrl(groupCreateRequestDto.getPreSignedLogoImageUrl())
		);
		groupRepository.save(group);
		GroupRegistration groupRegistration = GroupRegistration.of(
				now,
				member,
				group
		);
		groupRegistrationRepository.save(groupRegistration);
	}
}
