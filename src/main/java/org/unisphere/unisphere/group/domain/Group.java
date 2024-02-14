package org.unisphere.unisphere.group.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.unisphere.unisphere.member.domain.Member;

@Entity(name = "group_entity")
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@Column
	private String summary;

	@Column(columnDefinition = "text")
	private String content;

	@Column
	private String email;

	@Column
	private String avatarImageUrl;

	@Column
	private String logoImageUrl;

	@Column
	private String groupSiteUrl;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime approvedAt;

	@ToString.Exclude
	@OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<GroupRegistration> groupRegistrations = new ArrayList<>();

	@JoinColumn(name = "ownerMemberId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Member ownerMember;

	public static Group createGroup(LocalDateTime now, Member member, String name, String summary,
			String logoImageUrl) {
		Group group = new Group();
		group.name = name;
		group.summary = summary;
		group.createdAt = now;
		group.ownerMember = member;
		group.approvedAt = null;
		group.logoImageUrl = logoImageUrl;
		return group;
	}
}
