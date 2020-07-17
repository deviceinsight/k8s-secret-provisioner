package com.deviceinsight.k8s.secretprovisioner.config;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
public class ManagedSecret {
	
	private SecretIdentifier identifier;

	@ToString.Exclude
	private Map<String, String> secrets;
	
}
