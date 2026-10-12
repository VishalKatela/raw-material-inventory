# Problem Statement

## Business problem
Manufacturing and warehouse teams often struggle to monitor raw material availability in real time. Manual stock checks, scattered spreadsheets, and inconsistent transaction records lead to stockouts, excess inventory, and delays in production.

## Target users
- warehouse supervisors
- production planners
- purchasing teams
- inventory controllers
- operations managers

## Pain points
- no central view of material stock levels
- poor visibility into low-stock and out-of-stock items
- delayed update of stock movements
- lack of traceability for material inflow and outflow
- inconsistent status reporting across teams

## Stakeholders
- warehouse managers
- production managers
- procurement staff
- operations leadership
- IT/development team
- QA and release team

## Constraints
- must fit within a small MVP budget and timeline
- application should run locally and in a simple deployment environment
- data needs to remain accurate and auditable
- system should support basic multi-user operational use without enterprise complexity

## Success criteria
- users can add and update material records quickly
- stock movement transactions are recorded accurately
- low-stock and out-of-stock statuses are visible in the dashboard
- users can find materials by code or name
- the system supports a repeatable dev-to-deploy workflow

## MVP scope
The MVP will focus on:
- material catalogue
- transaction creation and update
- inventory status visibility
- search and filter
- exception alerts for low stock and out-of-stock conditions

## 15-week delivery split
- weeks 1-3: discovery, planning, and project setup
- weeks 4-8: core MVP implementation
- weeks 9-12: CI/CD, automation, testing, and containerization
- weeks 13-15: verification, release readiness, and documentation
