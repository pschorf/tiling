#!/bin/bash

CONTAINER=$(docker run -d -P -v $(pwd):/tiling pschorf/tiling)
echo $CONTAINER
docker inspect -f "{{ .NetworkSettings.Ports }}" $CONTAINER
