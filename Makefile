# Project variables
IMAGE_NAME   := gghoshdocker/robo-bob
IMAGE_TAG    := 1.0
CONTAINER    := $(IMAGE_NAME):$(IMAGE_TAG)

# K8s manifest files
K8S_DEPLOYMENT := k8s/robo-bob-deployment.yaml
K8S_SERVICE    := k8s/robo-bob-service.yaml

# 1) Build: compile & package the Spring Boot JAR
build:
\tmvn clean package

# 2) Docker Build
docker:
\tdocker build -t $(CONTAINER) .

# 3) Docker Push
push:
\tdocker push $(CONTAINER)

# 4) Kubernetes Deployment
deploy:
\tkubectl apply -f $(K8S_DEPLOYMENT)
\tkubectl apply -f $(K8S_SERVICE)

# Convenience target: build jar, then docker, then push
docker-push: build docker push