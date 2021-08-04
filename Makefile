docker-build:
	docker build -t games -f docker/Dockerfile .

docker-run:
	docker run -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$(shell ipconfig getifaddr en0):0 games
