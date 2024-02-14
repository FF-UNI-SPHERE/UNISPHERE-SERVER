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

	// 개인 이름으로 소식지 투고
	// POST /api/v1/articles/members/me
	@Operation(summary = "개인 이름으로 소식지 투고", description = "개인 이름으로 소식지를 투고합니다. 유니스피어 관리자의 승인이 필요합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@PostMapping(value = "/members/me")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured(MemberRole.S_USER)
	public void submitArticleAsMember(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestBody ArticleSubmissionRequestDto articleSubmissionRequestDto
	) {
	}

	// 단체 이름으로 소식지 투고
	// POST /api/v1/articles/groups/{groupId}
	@Operation(summary = "단체 이름으로 소식지 투고", description = "단체 이름으로 소식지를 투고합니다. 단체 관리자 등급 이상만 요청할 수 있습니다. 유니스피어 관리자의 승인이 필요합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@PostMapping(value = "/groups/{groupId}")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured(MemberRole.S_USER)
	public void submitArticleAsGroup(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@RequestBody ArticleSubmissionRequestDto articleSubmissionRequestDto
	) {
	}

	// 소식지 투고 승인
	// PATCH /api/v1/articles/{articleId}/accept (pending)
	@Operation(summary = "소식지 투고 승인", description = "소식지 투고를 승인합니다. 유니스피어 관리자만 요청할 수 있습니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PatchMapping(value = "/{articleId}/accept")
	@Secured(MemberRole.S_ADMIN)
	public void acceptArticle(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("articleId") Long articleId
	) {
	}

	// 소식지 편집
	// PUT /api/v1/articles/{articleId} (pending)
	@Operation(summary = "소식지 편집", description = "특정 소식지를 편집합니다. 소식지 작성자, 단체 관리자(단체의 소식지인 경우), 유니스피어 관리자만 요청할 수 있습니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok"),
	})
	@PutMapping(value = "/{articleId}")
	@Secured(MemberRole.S_USER)
	public void updateArticle(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("articleId") Long articleId,
			@RequestBody ArticleSubmissionRequestDto articleSubmissionRequestDto
	) {
	}

	// 소식지 삭제
	// DELETE /api/v1/articles/{articleId} (pending)
	@Operation(summary = "소식지 삭제", description = "특정 소식지를 삭제합니다. 소식지 작성자, 단체 관리자(단체의 소식지인 경우), 유니스피어 관리자만 요청할 수 있습니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "no content"),
	})
	@DeleteMapping(value = "/{articleId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Secured(MemberRole.S_USER)
	public void deleteArticle(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("articleId") Long articleId
	) {
	}

	// 소식지 조회
	// GET /api/v1/articles/{articleId}
	@Operation(summary = "소식지 상세 조회", description = "소식지 상세 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@GetMapping(value = "/{articleId}")
	@Secured(MemberRole.S_USER)
	public ArticleDetailResponseDto getArticleDetail(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("articleId") Long articleId
	) {
		return ArticleDetailResponseDto.builder().build();
	}

	// 소식지 목록 조회
	// GET /api/v1/articles/all?page={page}&size={size}
	@Operation(summary = "전체 소식지 목록 조회", description = "전체 소식지 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@GetMapping(value = "/all")
	@Secured(MemberRole.S_USER)
	public ArticleListResponseDto getArticleList(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
		return ArticleListResponseDto.builder().build();
	}

	// 내 소식지 목록 조회
	// GET /api/v1/articles/members/me?page={page}&size={size} (pending)
	@Operation(summary = "내 소식지 목록 조회", description = "내 소식지 목록을 조회합니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@GetMapping(value = "/members/me")
	@Secured(MemberRole.S_USER)
	public ArticleListResponseDto getMyArticleList(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
		return ArticleListResponseDto.builder().build();
	}

	// 특정 회원의 소식지 목록 조회
	// GET /api/v1/articles/members/{memberId}?page={page}&size={size} (pending)
	@Operation(summary = "특정 회원의 소식지 목록 조회", description = "특정 회원의 소식지 목록을 조회합니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@GetMapping(value = "/members/{memberId}")
	@Secured(MemberRole.S_USER)
	public ArticleListResponseDto getMemberArticleList(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("memberId") Long memberId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
		return ArticleListResponseDto.builder().build();
	}

	// 특정 단체의 소식지 목록 조회
	// GET /api/v1/articles/groups/{groupId}?page={page}&size={size} (pending)
	@Operation(summary = "특정 단체의 소식지 목록 조회", description = "특정 단체의 소식지 목록을 조회합니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@GetMapping(value = "/groups/{groupId}")
	@Secured(MemberRole.S_USER)
	public ArticleListResponseDto getGroupArticleList(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("groupId") Long groupId,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
		return ArticleListResponseDto.builder().build();
	}

	// 관심 소식지 등록
	// PUT /api/v1/articles/{articleId}/like (pending)
	@Operation(summary = "관심 소식지 등록", description = "특정 소식지를 관심 소식지로 등록합니다. 이미 관심 소식지로 등록한 경우, 성공 응답을 주며 아무런 변화가 일어나지 않습니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "ok")
	})
	@PutMapping(value = "/{articleId}/like")
	@Secured(MemberRole.S_USER)
	public void likeArticle(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("articleId") Long articleId
	) {
	}

	// 관심 소식지 해제
	// DELETE /api/v1/articles/{articleId}/like (pending)
	@Operation(summary = "관심 소식지 해제", description = "특정 소식지를 관심 소식지에서 제거합니다.", deprecated = true)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "no content")
	})
	@DeleteMapping(value = "/{articleId}/like")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Secured(MemberRole.S_USER)
	public void unlikeArticle(
			@LoginMemberInfo MemberSessionDto memberSessionDto,
			@PathVariable("articleId") Long articleId
	) {
	}
}
