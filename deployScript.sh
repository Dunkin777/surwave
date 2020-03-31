#!/bin/bash -xe
cd /home/surwave/surwave-be
git checkout Develop
git pull
./gradlew clean build -x test
docker-compose build
docker stop surwave-be
docker-compose up -d
docker cp ~/.aws surwave-be:/root
