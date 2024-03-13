package org.unisphere.unisphere.utils.entity;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.member.domain.Member;

@Builder
public class TestMember implements TestEntity<Member, Long> {

	@Builder.Default
	private MemberRole DEFAULT_ROLE = MemberRole.MEMBER;
	@Builder.Default
	private LocalDateTime DEFAULT_CREATED_AT_VALUE = LocalDateTime.of(LocalDate.EPOCH,
			LocalTime.MIDNIGHT);
	@Builder.Default
	private String DEFAULT_DELETED_AT_VALUE = null;
	private String email;
	private String nickname;
	private String avatarImageUrl;

	public static Member asDefaultEntity() {
		return TestMember.builder()
				.email(UUID.randomUUID() + "@gmail.com")
				.nickname(UUID.randomUUID().toString())
				.avatarImageUrl("avatar-images/" + UUID.randomUUID() + "/avatar.png")
				.build().asEntity();
	}

	@Override
	public Member asEntity() {
		return Member.of(
				UUID.randomUUID() + "@gmail.com",
				UUID.randomUUID().toString(),
				DEFAULT_CREATED_AT_VALUE,
				DEFAULT_ROLE
		);
	}

	@Override
	public Member asMockEntity(Long id) {
		Member member = mock(Member.class);
		lenient().when(member.getId()).thenReturn(id);
		lenient().when(member.getEmail()).thenReturn(email);
		lenient().when(member.getNickname()).thenReturn(nickname);
		lenient().when(member.getAvatarImageUrl()).thenReturn(avatarImageUrl);
		return member;
	}
}