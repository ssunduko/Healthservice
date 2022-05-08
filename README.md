# Health-Service

The Health Module enables Health Workers to survey and assess the health needs of a community in order to provide targeted health services to both individuals and communities in need. To comply with HIPAA, the health module provides security and integrity of health data both during and after collection. It will incorporate the FHIR (Fast Healthcare Interoperability Resources) standard specification to ease data storage and exchange. The module will provide a channel through which local health experts and suppliers such as pharmacies and medical facilities can provide services to the relief organizations through transactions.

# Requirements

* Mock-Server
* Maven
* Java 16 JDK
* Docker
* Kubernets

# Installation Instructions

## Build
* mvn clean
* mvn package

## ECR Registry Login
* aws ecr get-login-password --region us-east-1
* aws ecr --region us-east-1 | docker login -u AWS -p <Above encrytped password> 776968139487.dkr.ecr.us-east-1.amazonaws.com/health-service

## Build Docker Container
* docker build --no-cache --tag=776968139487.dkr.ecr.us-east-1.amazonaws.com/health-service:latest .
* docker push 776968139487.dkr.ecr.us-east-1.amazonaws.com/health-service:latest

## Create Kubernets Cluster
* eksctl create cluster -f cluster.yaml

## Create Load Balancer
* kubectl create -f loadbalancer.yaml
