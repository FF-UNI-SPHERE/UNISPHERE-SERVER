package org.unisphere.unisphere.article.service;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unisphere.unisphere.article.dto.request.ArticleSubmissionRequestDto;
import org.unisphere.unisphere.article.dto.response.ArticleDetailResponseDto;
import org.unisphere.unisphere.article.infrastructure.ArticleRepository;
import org.unisphere.unisphere.article.domain.Article;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.infrastructure.GroupRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleFacadeService {

	private final ArticleRepository articleRepository;
	private final GroupRepository groupRepository;

	@Transactional
	public void submitArticleForSelf(ArticleSubmissionRequestDto articleSubmissionRequestDto) {
		// 개인 소식지 투고
		Article article = new Article();
		articleRepository.save(article);
	}

	@Transactional
	public void submitArticleForGroup(Long groupId, ArticleSubmissionRequestDto articleSubmissionRequestDto) {
		// 단체 소식지 투고
		Group group = groupRepository.findByName(groupId)
				.orElseThrow(() -> new EntityNotFoundException("해당 그룹을 찾을 수 없습니다."));
		Article article = new Article();
		articleRepository.save(article);
	}

	@Transactional
	public void acceptArticleSubmission(Long articleId) {
		//소식지 투고 승인 이건 어떻게 해야하남...

	}

	@Transactional
	public void editArticle(Long articleId, String newTitle, String newContent, String newThumbnailUrl) {
		// 소식지 편집
		Article existingArticle = articleRepository.findById(articleId)
				.orElseThrow(() -> new NotFoundException("소식지를 찾을 수 없습니다."));

		existingArticle.setTitle(newTitle);
		existingArticle.setContent(newContent);
		existingArticle.setThumbnailImageUrl(newThumbnailUrl);

		articleRepository.save(existingArticle);
	}

	@Transactional
	public void deleteArticle(Long articleId) {
		// 소식지 삭제
		articleRepository.deleteArticleBy(articleId);
	}

	@Transactional(readOnly = true)
	public ArticleDetailResponseDto getArticleDetail(Long articleId) {
		// 기사 검색 -> 검색 소식지 상세 정보를 조회 여기서 author은 어떻게 return 하지...?
		Article article = articleRepository.findById(articleId)
				.orElseThrow(() -> new NotFoundException("소식지를 찾을 수 없습니다."));

		return ArticleDetailResponseDto.builder()
				.articleId(article.getId())
				.title(article.getTitle())
				.content(article.getContent())
				.thumbnailImageUrl(article.getThumbnailImageUrl())
				.createdAt(article.getCreatedAt().toString())
				.updatedAt(article.getUpdatedAt().toString())
				.build();
	}



	@Transactional(readOnly = true)
	public List<ArticleListResponseDto> getAllArticles(int page, int size) {
		// 모든 소식지 조회
		return null;
	}

	@Transactional(readOnly = true)
	public List<ArticleListResponseDto> getMyArticles(Long memberId, int page, int size) {
		// 현재 사용자의 소식지 목록 조회

	}

	@Transactional(readOnly = true)
	public List<ArticleListResponseDto> getGroupArticles(Long groupId, int page, int size) {
		// 특정 단체의 소식지 목록 조회

	}

	@Transactional
	public void likeArticle(Long articleId, Long memberId) {
		// 소식지 like
	}

	@Transactional
	public void unlikeArticle(Long articleId, Long memberId) {
		// 소식지 dislike
	}
}
