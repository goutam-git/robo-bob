IMAGE_NAME   := gghoshdocker/robo-bob
IMAGE_TAG    := 1.2
CONTAINER    := $(IMAGE_NAME):$(IMAGE_TAG)

# K8s manifest files
K8S_DEPLOYMENT := k8s/robo-bob-deployment.yaml
K8S_SERVICE    := k8s/robo-bob-service.yaml

# 1) Build: compile & package the Spring Boot JAR
build:
	#mvn clean install  (if needed)
	mvn clean package

# 2) Docker Build
docker:
	docker build -t $(CONTAINER) .

# 3) Docker Push
push:
	docker push $(CONTAINER)

# 4) Kubernetes Deployment
deploy:
	kubectl apply -f $(K8S_DEPLOYMENT)
	kubectl apply -f $(K8S_SERVICE)