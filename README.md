# Raw Material Inventory System

## Overview
This project implements a raw material inventory management system designed for manufacturing and warehouse operations. It supports material catalog management, stock movement transactions, low-stock alerts, search/filtering, and a simple dashboard for operational visibility.

## Project goals
- Track raw materials centrally and consistently.
- Record stock-in and stock-out transactions with audit-friendly history.
- Monitor material status and flag low or zero stock conditions.
- Support local development and a repeatable delivery workflow.

## Tech stack
- Java 21
- Spring Boot 4.0.0
- Maven
- H2 in-memory database for local development
- MySQL is the intended persistent deployment database; production database wiring is not included yet
- Thymeleaf + HTML/JavaScript UI
- Tomcat embedded server

## Local development setup
1. Ensure Java 21 is installed. Maven is provided by the wrapper.
2. From this directory, run:

```bash
./mvnw spring-boot:run
```

3. Open http://localhost:8080

The default configuration uses an in-memory H2 database. Data is cleared when the application stops. MySQL is the planned persistent deployment option, but no production profile or external database configuration is currently supplied.

## MVP scope
The MVP includes:
- material catalogue
- create and update transactions
- stock and order status visibility
- search/filter support
- exception alerting for low stock and out-of-stock conditions

## Project workflow and deliverables
This repository contains the planning and delivery artifacts for the 15-week curriculum:
- problem definition and scope
- stakeholder analysis
- agile backlog and sprint plan
- architecture and requirements documentation
- Git and branching policy
- Jenkins pipeline and Docker deployment artifacts
- Selenium test plan and automation framework scaffold
- Ansible provisioning playbook under `ansible/`

## CI and deployment
GitHub Actions runs `./mvnw test package` for pushes and pull requests to `main` and `development`. The Jenkins pipeline additionally builds a Docker image, replaces the local application container, and checks the HTTP health endpoint. A Jenkins agent running the pipeline must have Docker installed and access to the Docker daemon.

The Ansible playbook provisions Java 21 and a systemd service on an Ubuntu host. See `docs/configuration-management.md` for inventory setup and execution. Provisioning the application does not provision MySQL; the current default H2 database is in-memory and is not suitable for persistent production data.

## Branch policy
Use the branch strategy described in `.github/BRANCH_POLICY.md`.

## Issue templates
Templates are available under `.github/ISSUE_TEMPLATE/`.

## License
This project is intended for academic and demonstration use.
