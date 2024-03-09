package org.unisphere.unisphere.article.domain;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.member.domain.Member;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long author_id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AuthorType authorType;

	@Column(nullable = false)
	private String title;

	@Column(columnDefinition = "text")
	private String content;

	@Column
	private String thumbnailImageUrl;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime updatedAt;

	@Column
	private LocalDateTime approvedAt;

	@ToString.Exclude
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<InterestedArticle> interestedArticles = new ArrayList<>();

	public void setContent(String content){
		this.content = content;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public void setThumbnailImageUrl(String thumbnailImageUrl){
		this.thumbnailImageUrl = thumbnailImageUrl;
	}
}
