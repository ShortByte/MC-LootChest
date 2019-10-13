#!/bin/bash

function mvn() {
	local mavenhome=/tmp/maven

	if [ "$(docker volume ls | grep mavenhome)" == "" ]
	then
		docker volume create mavenhome
		echo "Set write permission on volumes for docker group"
		echo "Need Group Setup of /var/lib/docker and Setgid on volumes"
		sudo chmod g+w `docker volume inspect mavenhome --format '{{.Mountpoint}}'`
	fi
	docker run \
		--rm \
		-u `id -u` \
		-e MAVEN_CONFIG=$mavenhome/.m2 \
		-v `pwd`:/opt \
    -v mavenhome:$mavenhome:rw \
		-w /opt \
		maven:3-jdk-8 mvn -Duser.home=$mavenhome $@
}

mvn compile
