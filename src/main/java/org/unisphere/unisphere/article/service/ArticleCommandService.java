package org.unisphere.unisphere.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.article.domain.Article;
import org.unisphere.unisphere.article.domain.AuthorType;
import org.unisphere.unisphere.article.dto.request.ArticleSubmissionRequestDto;
import org.unisphere.unisphere.article.infrastructure.ArticleRepository;

@Service
@RequiredArgsConstructor
@Logging
@Transactional
public class ArticleCommandService {
    private final ArticleRepository articleRepository;

    public void submitArticle(Long authorId, ArticleSubmissionRequestDto articleSubmissionRequestDto, AuthorType authorType) {
        Article article = Article.createArticle(
                articleSubmissionRequestDto.getTitle(),
                articleSubmissionRequestDto.getContent(),
                articleSubmissionRequestDto.getPreSignedThumbnailImageUrl(),
                authorType,
                authorId
        );
        articleRepository.save(article);
    }


}
