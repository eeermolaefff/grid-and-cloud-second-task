#
# Build stage
#
FROM node:18-alpine AS build

WORKDIR /home/app

COPY public/ ./public
COPY src/ ./src
COPY package.json .

RUN npm install   

ENV JQ_VERSION=1.6
RUN wget --no-check-certificate https://github.com/stedolan/jq/releases/download/jq-${JQ_VERSION}/jq-linux64 -O /tmp/jq-linux64
RUN cp /tmp/jq-linux64 /usr/bin/jq
RUN chmod +x /usr/bin/jq

ARG BACKEND_API
ENV API=$BACKEND_API

RUN jq --arg API ${API} '{"BACKEND_API": $API}' ./src/config.json > ./src/config.tmp.json && mv ./src/config.tmp.json ./src/config.json

#
# Package stage
#
FROM node:18-alpine

WORKDIR /home/app
COPY --from=build /home/app/ ./
CMD npm start
