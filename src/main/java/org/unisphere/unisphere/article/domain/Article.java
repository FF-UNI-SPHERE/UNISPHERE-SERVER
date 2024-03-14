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

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long authorId;

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


	public static Article createArticle(String title, String content, String thumbnailImageUrl, AuthorType authorType, Long authorId){
		Article article = new Article();
		article.title = title;
		article.content = content;
		article.thumbnailImageUrl = thumbnailImageUrl;
		article.createdAt = LocalDateTime.now();
		article.authorType = authorType;
		article.authorId = authorId;
		return article;
	}
}
