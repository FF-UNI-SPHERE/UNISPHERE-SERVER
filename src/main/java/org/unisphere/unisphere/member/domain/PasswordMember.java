package org.unisphere.unisphere.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "member_id", nullable = false)
	@ToString.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	private Member member;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	public static PasswordMember of(
			Member member, String username, String password
	) {
		PasswordMember passwordMember = new PasswordMember();
		passwordMember.member = member;
		passwordMember.username = username;
		passwordMember.password = password;
		return passwordMember;
	}
}
