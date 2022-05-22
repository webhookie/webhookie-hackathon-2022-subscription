#!/usr/bin/env bash

NAME="webhookie-subscription-sample"
VERSION="0.0.1"
REPO="hookiesolutions"

build() {
  echo "Building..."
  ./gradlew clean
  ./gradlew bootJar
}

load() {
  echo "Building docker image in  $(pwd)"
  docker build \
    -t $REPO/$NAME:$VERSION \
    -t $REPO/$NAME:1 \
    -t $REPO/$NAME:latest \
    --load \
    .
}

push() {
  echo "Building docker image in  $(pwd)"
  docker buildx build \
    --platform linux/amd64,linux/arm64 \
    -t $REPO/$NAME:$VERSION \
    -t $REPO/$NAME:1 \
    -t $REPO/$NAME:latest \
    --push \
    .
}

$1
