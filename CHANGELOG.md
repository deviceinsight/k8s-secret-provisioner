# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.4]
### Fixed
- Push Docker Image on the master branch instead of tags

## [1.0.3]
### Fixed
- Fix reference to environment variables in POM in order to fix publishing to Docker Hub from GitLab CI

## [1.0.2]
### Fixed
- Disable JAR deployment, since only the Docker Image is used
- Disable Helm Chart deployment, since this is done via GitHub Pages

## [1.0.1]
### Fixed
- Fixed the auth configuration for publishing to Docker Hub

## [1.0.0] - 2020-07-17
### Added
- Initial version of the k8s-secret-provisioner

[Unreleased]: https://github.com/deviceinsight/k8s-secret-provisioner/compare/1.0.4...HEAD
[1.0.4]: https://github.com/deviceinsight/k8s-secret-provisioner/releases/tag/1.0.4
[1.0.3]: https://github.com/deviceinsight/k8s-secret-provisioner/releases/tag/1.0.3
[1.0.2]: https://github.com/deviceinsight/k8s-secret-provisioner/releases/tag/1.0.2
[1.0.1]: https://github.com/deviceinsight/k8s-secret-provisioner/releases/tag/1.0.1
[1.0.0]: https://github.com/deviceinsight/k8s-secret-provisioner/releases/tag/1.0.0
