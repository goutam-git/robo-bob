IMAGE := gghoshdocker/robo-bob
TAG   := 1.2
CONTAINER := $(IMAGE):$(TAG)

build:
	mvn clean package

docker-build:
	docker build -t $(CONTAINER) .

docker-push:
	docker push $(CONTAINER)

k8-deploy:
	kubectl apply -f k8s/robo-bob-deployment.yaml
	kubectl apply -f k8s/robo-bob-service.yaml