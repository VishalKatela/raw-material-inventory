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
- MySQL database
- Thymeleaf + HTML/JavaScript UI
- Tomcat embedded server

## Local development setup
1. Ensure Java 21 and Maven are installed.
2. Start MySQL and create a database named `raw_material_inventory`.
3. Update credentials in `src/main/resources/application.properties` if necessary.
4. Run:

```bash
./mvnw spring-boot:run
```

5. Open http://localhost:8080

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
- Ansible provisioning files

## Branch policy
Use the branch strategy described in `.github/BRANCH_POLICY.md`.

## Issue templates
Templates are available under `.github/ISSUE_TEMPLATE/`.

## License
This project is intended for academic and demonstration use.
