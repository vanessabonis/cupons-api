#!/bin/bash
# Script para rodar Maven com Java 21 e settings customizado

export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

mvn -s settings-custom.xml "$@"

