# Git and GitHub Repository Initialization

## Repository status
The repository is already initialized locally and connected to GitHub with the remote URL:

- https://github.com/VishalKatela/raw-material-inventory.git

## Recommended branch strategy
- `main` for stable production-ready code
- `development` for integration work
- `feature/*` for new functionality
- `bugfix/*` for issues and fixes
- `release/*` for release preparation

## Initial commit guidance
Suggested initial commit message:
- `chore: initialize raw material inventory project skeleton`

## README
The root `README.md` has been added with project overview and local setup instructions.

## GitHub issue templates
Issue templates are available under:
- .github/ISSUE_TEMPLATE/bug_report.md
- .github/ISSUE_TEMPLATE/feature_request.md

## Branch policy
See `.github/BRANCH_POLICY.md` for naming and merge rules.

## Recommended workflow
1. Create `development` from `main`.
2. Create a feature branch for each workstream.
3. Commit in small logical units.
4. Open a pull request into `development`.
5. Merge only after review and successful CI.

## Example commands
```bash
git checkout -b development
git checkout -b feature/material-catalog
git add .
git commit -m "feat: add material catalog entity and service"
git push origin feature/material-catalog
git checkout development
git pull origin development
git merge --no-ff feature/material-catalog
```
