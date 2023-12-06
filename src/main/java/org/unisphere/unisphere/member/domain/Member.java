package org.unisphere.unisphere.member.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.unisphere.unisphere.article.domain.InterestedArticle;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.auth.domain.OauthType;
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
	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private PasswordMember passwordMember;

	@ToString.Exclude
	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private SocialMember socialMember;

	@ToString.Exclude
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<GroupRegistration> groupRegistrations = new ArrayList<>();

	@ToString.Exclude
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<InterestedArticle> interestedArticles = new ArrayList<>();

	@Transient
	private boolean isFirstLogin = false;

	public static Member createSocialMember(
			String email, String nickname, LocalDateTime now, MemberRole role,
			String oauthId, OauthType oauthType
	) {
		Member member = new Member();
		member.role = role;
		member.email = email;
		member.nickname = nickname;
		member.createdAt = now;
		member.socialMember = SocialMember.of(member, oauthId, oauthType);
		member.isFirstLogin = true;
		return member;
	}

	public void updateAvatar(String nickname, String preSignedAvatarImageUrl) {
		if (nickname != null) {
			this.nickname = nickname;
		}
		if (preSignedAvatarImageUrl != null) {
			this.avatarImageUrl = preSignedAvatarImageUrl;
		}
	}
}
