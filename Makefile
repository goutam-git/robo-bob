IMAGE := gghoshdocker/robo-bob
TAG   := 1.2
CONTAINER := $(IMAGE):$(TAG)

build:
	mvn clean package

docker:
	docker build -t $(CONTAINER) .

docker-build: docker

push:
	docker push $(CONTAINER)

deploy:
	kubectl apply -f k8s/robo-bob-deployment.yaml
	kubectl apply -f k8s/robo-bob-service.yaml