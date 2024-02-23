package org.unisphere.unisphere.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.annotation.LoginMemberInfo;
import org.unisphere.unisphere.article.dto.request.ArticleSubmissionRequestDto;
import org.unisphere.unisphere.article.dto.response.ArticleDetailResponseDto;
import org.unisphere.unisphere.article.dto.response.ArticleListResponseDto;
import org.unisphere.unisphere.article.service.ArticleFacadeService;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.auth.dto.MemberSessionDto;

@RestController
@RequiredArgsConstructor
@Logging
@RequestMapping("/api/v1/articles")
@Tag(name = "소식지 (Article)", description = "소식지 관련 API")
public class ArticleController {

	private final ArticleFacadeService articleFacadeService;

	@RestController
	@RequiredArgsConstructor
	@Logging
	@RequestMapping("/api/v1/articles")
	@Tag(name = "소식지 (Article)", description = "소식지 관련 API")
	public class ArticleController {

		private final ArticleFacadeService articleFacadeService;

		// 개인 이름으로 소식지 투고
		// POST /api/v1/articles/members/me
		@Operation(summary = "개인 이름으로 소식지 투고", description = "개인 이름으로 소식지를 투고합니다.")
		@ApiResponse(responseCode = "201", description = "Created")
		@PostMapping(value = "/members/me")
		public ResponseEntity<Void> submitArticleForSelf(@RequestBody ArticleSubmissionRequestDto articleSubmissionRequestDto) {
			articleFacadeService.submitArticleForSelf(articleSubmissionRequestDto);
			return ResponseEntity.status(201).build();
		}

		// 개인 이름으로 소식지 투고 (관리자 승인 필요)
		// POST /api/v1/articles/members/me/admin-approval
		@Operation(summary = "개인 이름으로 소식지 투고 (관리자 승인 필요)", description = "개인 이름으로 소식지를 투고하며, 관리자의 승인이 필요합니다.")
		@ApiResponse(responseCode = "201", description = "Created")
		@PostMapping(value = "/members/me/admin-approval")
		@ResponseStatus(value = HttpStatus.CREATED)
		@Secured(MemberRole.S_USER)
		public ResponseEntity<Void> submitArticleAsMemberWithAdminApproval(
				@LoginMemberInfo MemberSessionDto memberSessionDto,
				@RequestBody ArticleSubmissionRequestDto articleSubmissionRequestDto) {
			articleFacadeService.submitArticleForSelfWithAdminApproval(articleSubmissionRequestDto);
			return ResponseEntity.status(201).build();
		}

		// 단체 이름으로 소식지 투고
		// POST /api/v1/articles/groups/{groupId}
		@Operation(summary = "단체 이름으로 소식지 투고", description = "단체이름으로 소식지를 투고합니다.")
		@ApiResponse(responseCode = "201", description = "Created")
		@PostMapping(value = "/groups/{groupId}")
		public ResponseEntity<Void> submitArticleForGroup(
				@PathVariable Long groupId,
				@RequestBody ArticleSubmissionRequestDto articleSubmissionRequestDto) {
			articleFacadeService.submitArticleForGroup(groupId, articleSubmissionRequestDto);
			return ResponseEntity.status(201).build();
		}

		// 소식지 투고 승인
		// PATCH /api/v1/articles/{articleId}/accept (pending)
		@Operation(summary = "소식지 투고 승인", description = "소식지 투고를 승인합니다. ")
		@ApiResponse(responseCode = "200", description = "ok")
		@PatchMapping(value = "/{articleId}/accept")
		public ResponseEntity<Void> acceptArticleSubmission(@PathVariable Long articleId) {
			articleFacadeService.acceptArticleSubmission(articleId);
			return ResponseEntity.ok().build();
		}

		// 소식지 편집
		// PUT /api/v1/articles/{articleId} (pending)
		@Operation(summary = "소식지 편집", description = "소식지를 편집합니다. ")
		@ApiResponse(responseCode = "200", description = "ok")
		@PutMapping(value = "/{articleId}")
		public ResponseEntity<Void> editArticle(
				@PathVariable Long articleId,
				@RequestBody ArticleSubmissionRequestDto editedArticleDto) {
			articleFacadeService.editArticle(articleId, editedArticleDto);
			return ResponseEntity.ok().build();
		}

		// 소식지 삭제
		// DELETE /api/v1/articles/{articleId} (pending)
		@Operation(summary = "소식지 삭제", description = "소식지를 삭제합니다. ")
		@ApiResponse(responseCode = "204", description = "no content")
		@DeleteMapping(value = "/{articleId}")
		public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
			articleFacadeService.deleteArticle(articleId);
			return ResponseEntity.noContent().build();
		}

		// 소식지 조회
		// GET /api/v1/articles/{articleId}
		@Operation(summary = "소식지 상세 조회", description = "소식지 상세 정보를 조회합니다.")
		@ApiResponse(responseCode = "200", description = "ok")
		@GetMapping(value = "/{articleId}")
		@Secured(MemberRole.S_USER)
		public ArticleDetailResponseDto getArticleDetail(
				@LoginMemberInfo MemberSessionDto memberSessionDto,
				@PathVariable("articleId") Long articleId
		) {
			return articleFacadeService.getArticleDetail(articleId);
		}

		// 소식지 목록 조회
		// GET /api/v1/articles/all?page={page}&size={size}
		@Operation(summary = "소식지 목록 조회", description = "모든 소식지를 조회합니다.")
		@ApiResponse(responseCode = "200", description = "ok")
		@GetMapping(value = "/all")
		public ResponseEntity<List<ArticleListResponseDto>> getAllArticles(
				@RequestParam(name = "page", defaultValue = "1") int page,
				@RequestParam(name = "size", defaultValue = "10") int size) {
			List<ArticleListResponseDto> articles = articleFacadeService.getAllArticles(page, size);

		// 내 소식지 목록 조회
		// GET /api/v1/articles/members/me?page={page}&size={size} (pending)
		@Operation(summary = "내 소식지 목록 조회", description = "현재 사용자의 소식지 목록을 조회합니다.")
		@ApiResponse(responseCode = "200", description = "OK")
		@GetMapping(value = "/members/me")
		public ResponseEntity<List<ArticleListResponseDto>> getMyArticles(
				@RequestParam(name = "page", defaultValue = "1") int page,
				@RequestParam(name = "size", defaultValue = "10") int size,
				@LoginMemberInfo MemberSessionDto memberSessionDto) {
			List<ArticleListResponseDto> myArticles = articleFacadeService.getMyArticles(memberSessionDto.getId(), page, size);
			return ResponseEntity.ok(myArticles);
		}

		// 특정 회원의 소식지 목록 조회
		// GET /api/v1/articles/members/{memberId}?page={page}&size={size} (pending)
		@Operation(summary = "특정 회원의 소식지 목록 조회", description = "특정 회원의 소식지 목록을 조회합니다.")
		@ApiResponse(responseCode = "200", description = "OK")
		@GetMapping(value = "/members/{memberId}")
		public ResponseEntity<List<ArticleListResponseDto>> getMemberArticles(
				@PathVariable Long memberId,
				@RequestParam(name = "page", defaultValue = "1") int page,
				@RequestParam(name = "size", defaultValue = "10") int size) {
			List<ArticleListResponseDto> memberArticles = articleFacadeService.getMemberArticles(memberId, page, size);
			return ResponseEntity.ok(memberArticles);
		}

		// 특정 단체의 소식지 목록 조회
		// GET /api/v1/articles/groups/{groupId}?page={page}&size={size} (pending)
		@Operation(summary = "특정 단체의 소식지 목록 조회", description = "특정 단체의 소식지 목록을 조회합니다.")
		@ApiResponse(responseCode = "200", description = "OK")
		@GetMapping(value = "/groups/{groupId}")
		public ResponseEntity<List<ArticleListResponseDto>> getGroupArticles(
				@PathVariable Long groupId,
				@RequestParam(name = "page", defaultValue = "1") int page,
				@RequestParam(name = "size", defaultValue = "10") int size) {
			List<ArticleListResponseDto> groupArticles = articleFacadeService.getGroupArticles(groupId, page, size);
			return ResponseEntity.ok(groupArticles);
		}

		// 관심 소식지 등록
		// PUT /api/v1/articles/{articleId}/like (pending)
		@Operation(summary = "관심 소식지 등록", description = "소식지를 관심 목록에 등록합니다.")
		@ApiResponse(responseCode = "200", description = "OK")
		@PutMapping(value = "/{articleId}/like")
		public ResponseEntity<Void> likeArticle(@PathVariable Long articleId,
				@LoginMemberInfo MemberSessionDto memberSessionDto) {
				articleFacadeService.likeArticle(articleId, memberSessionDto.getId());
				return ResponseEntity.ok().build();
		}

		// 관심 소식지 해제
		// DELETE /api/v1/articles/{articleId}/like (pending)
		@Operation(summary = "관심 소식지 해제", description = "소식지를 관심 목록에서 해제합니다.")
		@ApiResponse(responseCode = "200", description = "OK")
		@DeleteMapping(value = "/{articleId}/like")
		public ResponseEntity<Void> unlikeArticle(@PathVariable Long articleId,
				@LoginMemberInfo MemberSessionDto memberSessionDto) {
				articleFacadeService.unlikeArticle(articleId, memberSessionDto.getId());
				return ResponseEntity.ok().build();
		}

