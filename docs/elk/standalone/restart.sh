#!/usr/bin/env bash

echo 'restart...'
docker-compose down --volumes
docker-compose up -d --build
