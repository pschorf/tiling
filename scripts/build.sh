#!/bin/bash

lein uberjar
docker build -t pschorf/tiling -f Dockerfile.prod .
docker push pschorf/tiling
