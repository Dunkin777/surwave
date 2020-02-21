#!/bin/bash -xe
cd /home/surwave/surwave-be
git checkout Develop
git pull
./gradlew clean build -x test
docker-compose build core
docker stop surwave
docker-compose up -d core
