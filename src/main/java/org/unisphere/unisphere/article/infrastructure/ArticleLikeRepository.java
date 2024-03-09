package org.unisphere.unisphere.article.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.unisphere.unisphere.article.domain.InterestedArticle;
/*
사용자가 좋아요 누른 article list
 */
public interface ArticleLikeRepository {
    @Query("SELECT I.article.id FROM InterestedArticle I WHERE I.member.id = :memberId")
    List<Long> findArticleIdsByMemberId(Long memberId);
}
