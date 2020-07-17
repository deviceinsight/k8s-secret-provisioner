
package com.deviceinsight.k8s.secretprovisioner;

import com.deviceinsight.k8s.secretprovisioner.config.InvalidConfigurationFileException;
import com.deviceinsight.k8s.secretprovisioner.config.SecretConfiguration;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@Log
public class SecretProvisionerApp {

	public static void main(String[] args) throws IOException {
		var configurationFileName = args.length > 0 ? args[0] : "secrets.yaml";
		var configurationFile = new File(configurationFileName);
		if (!configurationFile.exists()) {
			throw new InvalidConfigurationFileException(String.format("File %s does not exist", configurationFile));
		}

		var config = SecretConfiguration.fromFile(configurationFile);
		try (var client = new DefaultKubernetesClient()) {
			new SecretProvisioner().provisionSecrets(client, config);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "Failed to provision secrets", e);
			System.exit(1);
		}
		
		System.exit(0);
	}

}
