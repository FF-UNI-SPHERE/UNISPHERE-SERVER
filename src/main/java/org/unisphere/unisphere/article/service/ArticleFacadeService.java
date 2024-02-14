package org.unisphere.unisphere.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unisphere.unisphere.article.infrastructure.ArticleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleFacadeService {

	private final ArticleRepository articleRepository;
}
