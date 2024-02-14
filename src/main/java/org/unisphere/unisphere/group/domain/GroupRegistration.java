package org.unisphere.unisphere.group.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.unisphere.unisphere.member.domain.Member;

@Entity(name = "group_registration")
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRegistration {

	@EmbeddedId
	@ToString.Exclude
	private GroupRegistrationCompositeKey id;

	@ManyToOne(optional = false, fetch = javax.persistence.FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false, insertable = false, updatable = false)
	private Member member;

	@ManyToOne(optional = false, fetch = javax.persistence.FetchType.LAZY)
	@JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
	private Group group;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private GroupRole role;

	@Column(nullable = true)
	private LocalDateTime registeredAt;

	public static GroupRegistration of(LocalDateTime now, Member ownerMember, Group group) {
		GroupRegistration groupRegistration = new GroupRegistration();
		groupRegistration.id = new GroupRegistrationCompositeKey(ownerMember.getId(),
				group.getId());
		groupRegistration.member = ownerMember;
		groupRegistration.group = group;
		groupRegistration.role = GroupRole.OWNER;
		groupRegistration.registeredAt = now;
		return groupRegistration;
	}
}
