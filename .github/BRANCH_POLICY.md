# Branch Naming and Workflow Policy

## Branch naming
- feature/<short-name>
- bugfix/<short-name>
- hotfix/<short-name>
- release/<version>
- chore/<short-name>

Examples:
- feature/material-catalog
- feature/transaction-management
- bugfix/stock-status-calculation

## Workflow
1. Create a feature branch from `development`.
2. Commit small, reviewable changes.
3. Open a pull request to `development`.
4. Require review and successful checks before merge.
5. Merge using a clean linear history where possible.
6. Tag release candidates on `main`.

## Protected branches
- `main`: production-ready baseline
- `development`: integration branch for validated work

## Commit guidance
- Use clear, action-oriented messages.
- Keep one topic per commit.
- Reference issue numbers where relevant.
