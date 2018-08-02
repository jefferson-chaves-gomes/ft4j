#!/bin/bash 

cd /home/
curl -O https://storage.googleapis.com/bionimbuzjefferson/play-1.4.4.zip
curl -O https://storage.googleapis.com/bionimbuzjefferson/bionimbuz.zip
apt-get update
apt-get install -y openjdk-8-jdk
apt-get install -y unzip
apt-get install -y python
unzip play-1.4.4.zip
unzip bionimbuz.zip
cd ./bionimbuz
/home/play-1.4.4/play deps
/home/play-1.4.4/play start
/home/play-1.4.4/play out
