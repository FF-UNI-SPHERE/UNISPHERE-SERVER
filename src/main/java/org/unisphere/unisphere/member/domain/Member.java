package org.unisphere.unisphere.member.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.unisphere.unisphere.article.domain.InterestedArticle;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRegistration;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column(nullable = true)
	private String avatarImageUrl;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = true)
	private LocalDateTime deletedAt;

	@ToString.Exclude
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<GroupRegistration> groupRegistrations = new ArrayList<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<InterestedArticle> interestedArticles = new ArrayList<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "ownerMember", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private final List<Group> managingGroups = new ArrayList<>();

	@Transient
	private boolean isFirstLogin = false;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Member)) {
			return false;
		}
		Member that = (Member) o;
		return id != null && id.equals(that.getId());
	}

	public static Member of(
			String email, String nickname, LocalDateTime now, MemberRole role
	) {
		Member member = new Member();
		member.role = role;
		member.email = email;
		member.nickname = nickname;
		member.createdAt = now;
		member.isFirstLogin = true;
		return member;
	}

	public void updateAvatar(String nickname, String preSignedAvatarImageUrl) {
		if (nickname != null && !nickname.isEmpty()) {
			this.nickname = nickname;
		}
		if (preSignedAvatarImageUrl != null && !preSignedAvatarImageUrl.isEmpty()) {
			this.avatarImageUrl = preSignedAvatarImageUrl;
		}
	}
}
