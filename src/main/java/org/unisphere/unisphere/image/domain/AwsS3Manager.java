package org.unisphere.unisphere.image.domain;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Slf4j
public class AwsS3Manager {

	private final String bucketName;
	private final AmazonS3Client amazonS3Client;

	public AwsS3Manager(
			AmazonS3Client amazonS3Client,
			@Value("${cloud.aws.s3.bucket}") String bucketName) {
		this.amazonS3Client = amazonS3Client;
		this.bucketName = bucketName;
	}

	public void delete(String key) {
		log.info("deleting file from s3: {}", key);
		try {
			amazonS3Client.deleteObject(bucketName, key);
		} catch (Exception e) {
			log.error("s3 delete error: {}", e.getMessage());
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
					"이미지 삭제에 실패했습니다. 다시 시도해주세요.");
		}
	}

	public String getObjectUrl(String key) {
		if (!doesObjectExist(key)) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
					"이미지가 존재하지 않습니다.");
		}
		return amazonS3Client.getUrl(bucketName, key).toString();
	}

	public String getPreSignedUrl(String objectKey) {
		log.info("getPreSignedUrl objectKey: {}", objectKey);
		GeneratePresignedUrlRequest request = createPreSignedUrlRequest(objectKey);
		String preSignedUrl = amazonS3Client.generatePresignedUrl(request).toString();
		log.info("preSignedUrl: {}", preSignedUrl);
		return preSignedUrl;
	}

	public boolean doesObjectExist(String objectKey) {
		return amazonS3Client.doesObjectExist(bucketName, objectKey);
	}

	private GeneratePresignedUrlRequest createPreSignedUrlRequest(String objectKey) {
		return new GeneratePresignedUrlRequest(
				bucketName, objectKey)
				.withMethod(HttpMethod.PUT)
				.withExpiration(getPreSignedUrlExpiration());
	}

	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60; // 1 hour
		expiration.setTime(expTimeMillis);
		return expiration;
	}
}
