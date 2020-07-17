# k8s-secret-provisioner

Small application ensuring configured secret data is present in the kubernetes cluster.
It will only:

* create non existing secrets
* add non existing secret keys

Motivation is to use this application together with helm generated random keys, without always modifying already present secrets.
This will allow rolling upgrades, since present applications do not need to refresh their secrets.

The k8s-secret-provisioner also comes with a helm chart, providing a job to be executed as a [helm hook](https://github.com/kubernetes/helm/blob/master/docs/charts_hooks.md).

The format of the secret values (base64 encoding, private keys, ...) is transparent for the application.
It is handled by the helm chart, which provides explicit support for passwords and private keys, using [sprig functions](http://masterminds.github.io/sprig/).

## Using the k8s-secret-provisioner

The following example demonstrates the use for a service `myservice` that needs a system account at a different service.

The Helm release name of this example is `demo`.

The helm chart of the service contains the following `values.yaml`:


```yaml
otherService:
  serviceName: other-service
  servicePort: 8080
  systemUser:
    user: _myservice_user
    secretName: myservice
    secretKey: other-service-password
    system: true
    authorities:
      - READ_ASSET
      - CREATE_ASSET
      - READ_ASSET_GROUP
      - READ_ASSET_GROUP_DEFINITION
```

These values are used in the `deployment.yaml` to create a deployment descriptor that takes the
username and password for the `myservice` to connect to the other service:

```yaml
kind: Deployment
spec:
  template:
    spec:
      containers:
        - name: {{ .Chart.Name }}
          env:
            - name: OTHER_SERVICE_URL
              value: http://{{ .Values.otherService.serviceName }}:{{ .Values.otherService.servicePort }}
            - name: OTHER_SERVICE_USERNAME
              value: {{ .Values.otherService.systemUser.user }}
            - name: OTHER_SERVICE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: {{ list .Release.Name .Values.otherService.systemUser.secretName  | join "-" }}
                  key: {{ .Values.otherService.systemUser.secretKey }}
```

Now, the `myservice` uses the K8s secret `demo-myservice` that contains the password in the key `other-service-password`.

The next step is to configure the k8s-secret-provisioner to automatically create the secret and to configure
the other service to provide the system user `_myservice_user` with that password.

This can be achieved by telling Helm to copy the values to configuration of the k8-secret-provisioner
and the other service. This is done in the `requirements.yaml` of the Chart that includes all services:

```yaml
dependencies:
  - name: myservice
    version: ${myservice.version}
    repository: ${myservice.repository}
    condition: global.myservice.enabled
    import-values:
      - child: otherService.systemUser
        parent: other.other-service.systemUsers.myservice
      - child: otherService.systemUser
        parent: other.k8s-secret-provisioner.passwords.myservice
  - name: other
    version: ${other.version}
    repository: ${other.repository}
```

### Used variables

The following table shows which values of the configuration are used in the k8s-secret-provisioner
and in the other service:

| variable | Used in k8s-secret-provisioner | Used in the other service |
| --- | :---: | :---: |
| user | | X |
| secretName | X | X |
| secretKey | X | X |
| prefixRelease | X | X |
| system | | X |
| authorities | | X |
