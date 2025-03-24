package com.deviceinsight.k8s.secretprovisioner;

import com.deviceinsight.k8s.secretprovisioner.config.ManagedSecret;
import com.deviceinsight.k8s.secretprovisioner.config.SecretConfiguration;
import com.deviceinsight.k8s.secretprovisioner.config.SecretIdentifier;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
public class SecretProvisioner {

	public void provisionSecrets(KubernetesClient client, SecretConfiguration config) {
		log.info("Provisioning secrets with configuration " + config);

		var managedSecrets = config.getSecrets().stream()
				.collect(Collectors.toMap(ManagedSecret::getIdentifier, ManagedSecret::getSecrets,
						(s1, s2) -> {
							s1.putAll(s2);
							return s1;
						}));

		managedSecrets.forEach((k, v) -> ensureSecretExists(client, k, v, config.getRelease()));
	}

	private static void ensureSecretExists(KubernetesClient client, SecretIdentifier identifier,
			Map<String, String> secretData, String release) {

		var secret = Optional.ofNullable(
				client.secrets().inNamespace(identifier.getNamespace()).withName(identifier.getName()).get());
		if (secret.isPresent()) {
			editSecret(client, secret.get(), secretData);
		} else {
			createSecret(client, identifier, secretData, release);
		}
	}

	private static void createSecret(KubernetesClient client, SecretIdentifier identifier,
			Map<String, String> secretData, String release) {

		log.info(String.format("Creating secret %s with %s keys", identifier, secretData.keySet()));
		client.secrets().inNamespace(identifier.getNamespace()).resource(new SecretBuilder()
				.withNewMetadata()
				.withName(identifier.getName())
				.addToLabels("release", release)
				.addToLabels("app", "k8s-secret-provisioner")
				.endMetadata().addToData(secretData)
				.build())
				.create();
	}

	private static void editSecret(KubernetesClient client, Secret secret, Map<String, String> secretData) {
		var metaData = secret.getMetadata();
		var existingKeys = secret.getData().keySet();
		var secretDataToAdd = secretData.entrySet().stream()
				.filter((e) -> !existingKeys.contains(e.getKey()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		if (!secretDataToAdd.isEmpty()) {
			log.info(String.format("Adding secret keys %s to secret %s", metaData, secretDataToAdd));
			client.secrets()
					.inNamespace(metaData.getNamespace())
					.withName(metaData.getName())
					.edit(s -> new SecretBuilder(s).addToData(secretDataToAdd).build());
		} else {
			log.info(String.format("Skipping modification of secret %s, since all keys are present", metaData));
		}
	}
}
