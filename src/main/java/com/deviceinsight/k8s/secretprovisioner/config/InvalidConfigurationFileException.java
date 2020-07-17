package com.deviceinsight.k8s.secretprovisioner.config;


@SuppressWarnings("serial")
public class InvalidConfigurationFileException extends RuntimeException {

	public InvalidConfigurationFileException() {
	}

	public InvalidConfigurationFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidConfigurationFileException(String message) {
		super(message);
	}

	public InvalidConfigurationFileException(Throwable cause) {
		super(cause);
	}
	
		

}
