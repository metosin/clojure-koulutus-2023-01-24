#!/usr/bin/env bash

set -e

docker build -t koulutus:dev ./docker/dev
docker build -t koulutus:server -f ./docker/server/Dockerfile .
docker build -t koulutus:web -f ./docker/web/Dockerfile .
