package org.unisphere.unisphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UnisphereApplication {

	public static void main(String[] args) {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
		SpringApplication.run(UnisphereApplication.class, args);
	}

}
