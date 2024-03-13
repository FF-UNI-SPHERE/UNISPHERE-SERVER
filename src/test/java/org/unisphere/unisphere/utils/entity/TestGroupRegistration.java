package org.unisphere.unisphere.utils.entity;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRegistration;
import org.unisphere.unisphere.group.domain.GroupRole;
import org.unisphere.unisphere.member.domain.Member;

@Builder
public class TestGroupRegistration implements TestEntity<GroupRegistration, Long> {

	private GroupRole role;
	private LocalDateTime registeredAt;
	private Member member;
	private Group group;

	public static GroupRegistration asOwnerRegistration(Member member, Group group) {
		return TestGroupRegistration.builder()
				.role(GroupRole.OWNER)
				.registeredAt(LocalDateTime.of(LocalDate.EPOCH, LocalTime.MIDNIGHT))
				.member(member)
				.group(group)
				.build().asEntity();
	}

	public static GroupRegistration asAdminRegistration(Member member, Group group) {
		return TestGroupRegistration.builder()
				.role(GroupRole.ADMIN)
				.registeredAt(LocalDateTime.of(LocalDate.EPOCH, LocalTime.MIDNIGHT))
				.member(member)
				.group(group)
				.build().asEntity();
	}

	public static GroupRegistration asNotApprovedCommonRegistration(Member member, Group group) {
		return TestGroupRegistration.builder()
				.role(GroupRole.COMMON)
				.registeredAt(null)
				.member(member)
				.group(group)
				.build().asEntity();
	}

	public static GroupRegistration asApprovedCommonRegistration(Member member, Group group) {
		return TestGroupRegistration.builder()
				.role(GroupRole.COMMON)
				.registeredAt(LocalDateTime.of(LocalDate.EPOCH, LocalTime.MIDNIGHT))
				.member(member)
				.group(group)
				.build().asEntity();
	}

	public static List<GroupRegistration> asAdminRegistrations(int count, Member member,
			Group group) {
		return IntStream.range(0, count)
				.mapToObj(i -> asAdminRegistration(member, group))
				.collect(Collectors.toList());
	}

	public static List<GroupRegistration> asCommonRegistrations(int count, Member member,
			Group group) {
		return IntStream.range(0, count)
				.mapToObj(i -> asNotApprovedCommonRegistration(member, group))
				.collect(Collectors.toList());
	}

	@Override
	public GroupRegistration asEntity() {
		return GroupRegistration.of(
				member,
				group,
				role,
				registeredAt
		);
	}

	@Override
	public GroupRegistration asMockEntity(Long id) {
		GroupRegistration groupRegistration = mock(GroupRegistration.class);
		lenient().when(groupRegistration.getRole()).thenReturn(role);
		lenient().when(groupRegistration.getRegisteredAt()).thenReturn(registeredAt);
		lenient().when(groupRegistration.getMember()).thenReturn(member);
		lenient().when(groupRegistration.getGroup()).thenReturn(group);
		return groupRegistration;
	}
}
