package org.unisphere.unisphere.image.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.image.domain.ObjectStorageManager;
import org.unisphere.unisphere.log.LogLevel;

@Service
@RequiredArgsConstructor
@Logging(level = LogLevel.DEBUG)
@Transactional
public class ImageService {

	@Value("${unisphere.images.path.article}")
	private String ARTICLE_IMAGE_DIR;

	@Value("${unisphere.images.path.avatar}")
	private String AVATAR_IMAGE_DIR;

	@Value("${unisphere.images.path.logo}")
	private String LOGO_IMAGE_DIR;

	private static final List<String> validImageExtension = List.of(".jpg", ".jpeg", ".png");
	private final ObjectStorageManager objectStorageManager;


	public String getPreSignedUrl(String imageUrl) {
		if (!isValidDirName(imageUrl)) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
					imageUrl + "은 유효하지 않은 파일 경로입니다. (유효한 파일 경로: " + getValidDirNameList() + ")");
		}
		if (!isImageExtension(getExtension(imageUrl))) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
					imageUrl + "은 유효하지 않은 파일 확장자입니다. (유효한 파일 확장자: " + validImageExtension + ")");
		}
		return objectStorageManager.getPreSignedUrl(imageUrl);
	}

	public String getImageUrl(String imageUrl) {
		if (Objects.isNull(imageUrl) || imageUrl.isBlank()) {
			return null;
		}
		return objectStorageManager.getObjectUrl(imageUrl);
	}

	public String findImageUrl(String imageUrl) {
		if (Objects.isNull(imageUrl) || imageUrl.isBlank()) {
			return null;
		}
		try {
			return objectStorageManager.getObjectUrl(imageUrl);
		} catch (Exception e) {
			return null;
		}
	}

	private List<String> getValidDirNameList() {
		return List.of(ARTICLE_IMAGE_DIR, AVATAR_IMAGE_DIR, LOGO_IMAGE_DIR);
	}

	private boolean isValidDirName(String imageUrl) {
		List<String> dirNameList = getValidDirNameList();
		String dirName = getDirName(imageUrl);
		if (Objects.isNull(dirName) || dirName.isBlank()) {
			return false;
		}
		return dirNameList.contains(dirName);
	}

	private String getDirName(String imageUrl) {
		if (Objects.isNull(imageUrl) || imageUrl.isBlank()) {
			return null;
		}
		return imageUrl.substring(0, imageUrl.indexOf("/") + 1);
	}

	private String getExtension(String filename) {
		if (!filename.contains(".")) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
					filename + "은 유효하지 않은 파일 이름입니다.");
		}
		return filename.substring(filename.lastIndexOf("."));
	}

	private boolean isImageExtension(String extension) {
		return validImageExtension.contains(extension);
	}
}
