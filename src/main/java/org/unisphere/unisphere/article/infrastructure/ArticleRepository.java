package org.unisphere.unisphere.article.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.unisphere.unisphere.article.domain.Article;
import org.unisphere.unisphere.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    // 소식지 DB에 추가
    @Override
    Article save(Article article);

    // 작성자 이름으로 검색
    @Query("select  a from Article  a where a.author_id = :author_id");
    Optional<Article> findByAuthor_id (int author_id);

    //소식지 id로 검색
    @Query("select a from Article a where a.id = :id");
    Optional<Article> findById(Long id);

    // 소식지 DB에서 삭제
    @Transactional
    @Modifying
    @Query("delete from Article a where a.id = :id")
    void deleteArticleBy(Long id);
}
