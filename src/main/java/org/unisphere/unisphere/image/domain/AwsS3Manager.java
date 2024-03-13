package org.unisphere.unisphere.image.domain;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.exception.ExceptionStatus;
import org.unisphere.unisphere.log.LogLevel;

@Component
@Logging(level = LogLevel.DEBUG)
public class AwsS3Manager implements ObjectStorageManager {

	private final String bucketName;
	private final AmazonS3Client amazonS3Client;

	public AwsS3Manager(
			AmazonS3Client amazonS3Client,
			@Value("${cloud.aws.s3.bucket}") String bucketName) {
		this.amazonS3Client = amazonS3Client;
		this.bucketName = bucketName;
	}

	@Override
	public void delete(String key) {
		try {
			amazonS3Client.deleteObject(bucketName, key);
		} catch (Exception e) {
			throw ExceptionStatus.FAILED_TO_DELETE_IMAGE.toDomainException();
		}
	}

	@Override
	public String getObjectUrl(String key) {
		if (!doesObjectExist(key)) {
			throw ExceptionStatus.NOT_FOUND_IMAGE.toDomainException();
		}
		return amazonS3Client.getUrl(bucketName, key).toString();
	}

	@Override
	public String getPreSignedUrl(String objectKey) {
		GeneratePresignedUrlRequest request = createPreSignedUrlRequest(objectKey);
		return amazonS3Client.generatePresignedUrl(request).toString();
	}

	@Override
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
