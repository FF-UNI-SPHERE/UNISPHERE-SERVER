package org.unisphere.unisphere.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.unisphere.unisphere.image.domain.ObjectStorageManager;

@TestConfiguration
public class ExternalDependenciesIgnore {

	@Bean
	public ObjectStorageManager objectStorageManager() {
		return new ObjectStorageManager() {
			@Override
			public void delete(String key) {
			}

			@Override
			public String getPreSignedUrl(String key) {
				return null;
			}

			@Override
			public String getObjectUrl(String key) {
				return null;
			}

			@Override
			public boolean doesObjectExist(String key) {
				return true;
			}
		};
	}
}
