#!/bin/bash 

cd /home/
curl -O https://storage.googleapis.com/bionimbuzjefferson/plugin-gcp-0.1.jar
curl -O https://storage.googleapis.com/bionimbuzjefferson/ft-coordinator-0.0.1-exec.jar
apt-get update
apt-get install -y openjdk-8-jdk
java -jar plugin-gcp-0.1.jar
