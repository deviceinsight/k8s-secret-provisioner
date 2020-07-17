package com.deviceinsight.k8s.secretprovisioner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Data
public class SecretConfiguration {

	private List<ManagedSecret> secrets;
	
	private String release;

	private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

	private SecretConfiguration() {

	}

	public static SecretConfiguration fromFile(File file) throws InvalidConfigurationFileException {
		try {
			return mapper.readValue(file, SecretConfiguration.class);
		} catch (IOException e) {
			throw new InvalidConfigurationFileException(e);
		}
	}

}
