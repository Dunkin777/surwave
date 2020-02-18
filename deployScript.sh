#!/bin/bash -xe
echo 'me script'
cd /home/surwave/surwave-be
git branch
git pull
./gradlew clean build
docker-compose build core
docker stop surwave
docker-compose up core
