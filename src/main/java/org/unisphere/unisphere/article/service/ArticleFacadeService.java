package org.unisphere.unisphere.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unisphere.unisphere.article.domain.AuthorType;
import org.unisphere.unisphere.article.dto.request.ArticleSubmissionRequestDto;
import org.unisphere.unisphere.article.infrastructure.ArticleRepository;
import org.unisphere.unisphere.exception.ExceptionStatus;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.service.GroupQueryService;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.member.service.MemberQueryService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleFacadeService {

	private final ArticleRepository articleRepository;
    private final ArticleCommandService articleCommandService;
    private final MemberQueryService memberQueryService;
    private final GroupQueryService groupQueryService;
    @Transactional
    public void submitMemberArticle(Long memberId, ArticleSubmissionRequestDto articleSubmissionRequestDto) {

        articleCommandService.submitArticle(memberId,articleSubmissionRequestDto, AuthorType.MEMBER);
    }

    @Transactional
    public void submitGroupArticle(Long memberId, Long authorId, ArticleSubmissionRequestDto articleSubmissionRequestDto) {
        Member member = memberQueryService.getMember(memberId);
        Group group = groupQueryService.getGroup(authorId);
        if (!groupQueryService.findGroupAdmins(group.getId()).contains(member)) {
            throw ExceptionStatus.NOT_GROUP_ADMIN.toServiceException();
        }
        articleCommandService.submitArticle(authorId, articleSubmissionRequestDto, AuthorType.GROUP);
    }
}

