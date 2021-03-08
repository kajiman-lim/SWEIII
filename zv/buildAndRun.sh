#!/bin/sh
mvn clean package && docker build -t de.hsb.app/zv .
docker rm -f zv || true && docker run -d -p 8080:8080 -p 4848:4848 --name zv de.hsb.app/zv 
