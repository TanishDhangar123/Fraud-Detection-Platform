# 🚀 Fraud Detection Platform

## 📌 Overview

This is a full-stack Fraud Detection Platform designed to simulate a real-world financial fraud detection system.

It processes transactions in real-time, applies fraud detection rules, securely stores data on AWS, and provides a dashboard for monitoring transactions and fraud insights.

---

## 🏗️ Architecture

Frontend (Vue.js)  
↓  
Authentication (Keycloak - OAuth2)  
↓  
Backend API (Spring Boot)  
↓  
AWS (DynamoDB + S3)  
↓  
Infrastructure (Terraform + Kubernetes)

---

## 📁 Project Structure

fraud-detection-platform/

- service/      → Spring Boot backend (Fraud API)  
- frontend/     → Vue.js dashboard  
- terraform/    → AWS infrastructure  
- k8s/          → Kubernetes manifests  
- jenkins/      → CI/CD scripts  
- README.md  

---

### Fraud Detection Logic

The system flags a transaction as fraudulent based on:

- Amount Threshold  
  Transactions above a predefined limit are marked suspicious  

- Velocity Check  
  Multiple transactions within a short time window (e.g., >5 transactions in 30 seconds)  

- Rule-Based Patterns  
  - Transactions from unusual locations  
  - Rapid repeated transactions  
  - High-risk merchant categories  

Each transaction is evaluated in real-time and assigned a fraud status.


## ⚙️ Backend – Fraud Detection API

### Tech Stack
- Java 17  
- Spring Boot  
- Spring Security (OAuth2 Resource Server)  
- AWS SDK (DynamoDB, S3)  
- Keycloak  
- Docker & Kubernetes  

### Responsibilities
- Accept transaction JSON  
- Apply fraud detection rules (amount, velocity, patterns)  
- Store transactions in DynamoDB  
- Store fraudulent transactions in S3  
- Secure APIs using JWT tokens  
- Provide APIs:
  - All transactions  
  - Fraud alerts  
  - Fraud summaries  

### Run Backend

```bash
cd service
mvn clean package
java -jar target/*.jar
