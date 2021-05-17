# cv-service-2

### Description
This project does not do much for now, it just publish 2 endpoints:
- `cv2/helloworld` which will responde with `helloworkd <uuid>` and produce event message `hello-cv2`
- `cv2/healthcheck`

It will be used by the cluster [cv-k8s](https://github.com/s1lv10fr4gn4n1-org/cv-k8s).


### Requirements
- [Gradle](https://gradle.org/)
- [JDK 1.8](https://www.oracle.com/de/java/technologies/javase/javase-jdk8-downloads.html)
- [Spring Native](https://github.com/spring-projects-experimental/spring-native)


### How to build
Run local using gradle
- `./gradlew bootRun`

Build container image 
- `./gradlew bootBuildImage`