package com.deviceinsight.k8s.secretprovisioner.config;

import lombok.Data;

@Data
public class SecretIdentifier {

	private String namespace;
	
	private String name;

}