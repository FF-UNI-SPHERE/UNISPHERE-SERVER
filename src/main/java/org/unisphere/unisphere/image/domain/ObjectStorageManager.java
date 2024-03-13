package org.unisphere.unisphere.image.domain;

public interface ObjectStorageManager {

	void delete(String key);

	String getPreSignedUrl(String key);

	String getObjectUrl(String key);

	boolean doesObjectExist(String key);
}
