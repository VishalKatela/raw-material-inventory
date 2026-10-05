# Feature Development with Branching

## Feature 1: Material catalog workflow
### Goal
Allow users to add and view material records through an operational inventory dashboard.

### Branch
- feature/material-catalog

### Acceptance criteria
- a user can enter a material code and name
- the system creates a material record with valid fields
- the system calculates stock status automatically
- the inventory list displays material rows in the dashboard

## Feature 2: Transaction processing workflow
### Goal
Support stock-in and stock-out flows so material movement is tracked.

### Branch
- feature/transaction-processing

### Acceptance criteria
- valid stock-in transactions increase quantity
- valid stock-out transactions reduce quantity when sufficient stock is available
- invalid quantities or types are rejected with a clear error
- transactions are saved with date and remarks

## Pull request workflow
1. Create feature branch from `development`
2. Implement focused changes
3. Commit with descriptive message
4. Push branch to origin
5. Open PR for review
6. Address review comments
7. Merge into `development`

## Merge conflict example
Conflicts often happen when two branches edit the same section of `README.md` or configuration files. Resolve by preserving the required project information and merging the intended content from both sources.

## Release ready baseline
The MVP should be tagged after confirmation that the core workflows pass and documentation is complete.
