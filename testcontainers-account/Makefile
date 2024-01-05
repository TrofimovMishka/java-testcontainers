IMAGE_NAME=test-containers-account
IMAGE_VERSION=1.0.6-SNAPSHOT

docker:
		docker build --pull --build-arg JAR_FILE=target/*.jar -t $(IMAGE_NAME):$(IMAGE_VERSION) .

