#!/bin/bash
echo "Installing local JARs into Maven..."
mvn install:install-file \
  -Dfile=lib/kiteconnect.jar \
  -DgroupId=com.zerodhatech \
  -DartifactId=kiteconnect \
  -Dversion=3.4.0 \
  -Dpackaging=jar
