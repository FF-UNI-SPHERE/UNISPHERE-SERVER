package org.unisphere.unisphere.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.image.dto.request.ImageRequestDto;
import org.unisphere.unisphere.image.service.ImageService;

@RestController
@RequiredArgsConstructor
@Logging
@RequestMapping("/api/v1/images")
@Tag(name = "이미지 (Image)", description = "이미지 관련 API")
public class ImageController {

	private final ImageService imageService;

	@Operation(summary = "이미지 presigned-url 발급", description = "이미지 업로드를 위한 presigned-url을 발급합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "created"),
	})
	@PostMapping(value = "/presigned-url")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Secured(MemberRole.S_USER)
	public String getPreSignedUrl(
			@RequestBody ImageRequestDto imageRequestDto
	) {
		return imageService.getPreSignedUrl(imageRequestDto.getImageUrl());
	}
}
