package org.unisphere.unisphere.utils.entity;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Builder;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.member.domain.Member;

@Builder
public class TestGroup implements TestEntity<Group, Long> {

	@Builder.Default
	private String DEFAULT_SUMMARY_VALUE = "summary";
	@Builder.Default
	private String DEFAULT_CONTENT_VALUE = "content";
	@Builder.Default
	private String DEFAULT_GROUP_SITE_URL_VALUE = "https://example.com";
	@Builder.Default
	private String DEFAULT_EMAIL_VALUE = "example@gmail.com";
	@Builder.Default
	private LocalDateTime DEFAULT_CREATED_AT_VALUE = LocalDateTime.of(LocalDate.EPOCH,
			LocalTime.MIDNIGHT);
	private Member ownerMember;
	private String avatarImageUrl;
	private String logoImageUrl;
	private String name;

	public static Group asDefaultEntity(Member ownerMember) {
		return TestGroup.builder()
				.logoImageUrl("logo-images/" + UUID.randomUUID() + "/logo.png")
				.avatarImageUrl("avatar-images/" + UUID.randomUUID() + "/avatar.png")
				.ownerMember(ownerMember)
				.build().asEntity();
	}

	public static List<Group> asDefaultEntities(int count, Member ownerMember) {
		return IntStream.range(0, count)
				.mapToObj(i -> asDefaultEntity(ownerMember))
				.collect(Collectors.toList());
	}

	@Override
	public Group asEntity() {
		return Group.createGroup(
				DEFAULT_CREATED_AT_VALUE,
				ownerMember,
				name != null ? name : UUID.randomUUID().toString(),
				DEFAULT_SUMMARY_VALUE,
				logoImageUrl != null ? logoImageUrl
						: "logo-images/" + UUID.randomUUID() + "/logo.png"
		);
	}

	@Override
	public Group asMockEntity(Long id) {
		Group group = mock(Group.class);
		lenient().when(group.getId()).thenReturn(id);
		lenient().when(group.getName()).thenReturn(UUID.randomUUID().toString());
		lenient().when(group.getSummary()).thenReturn(DEFAULT_SUMMARY_VALUE);
		lenient().when(group.getContent()).thenReturn(DEFAULT_CONTENT_VALUE);
		lenient().when(group.getGroupSiteUrl()).thenReturn(DEFAULT_GROUP_SITE_URL_VALUE);
		lenient().when(group.getCreatedAt()).thenReturn(DEFAULT_CREATED_AT_VALUE);
		lenient().when(group.getAvatarImageUrl())
				.thenReturn("avatar-images/" + UUID.randomUUID() + "/avatar.png");
		lenient().when(group.getLogoImageUrl())
				.thenReturn("logo-images/" + UUID.randomUUID() + "/logo.png");
		lenient().when(group.getApprovedAt()).thenReturn(null);
		return group;
	}
}
