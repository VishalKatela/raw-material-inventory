# Viva and Defense Preparation Guide: Automated Raw Material Inventory System

## Project Overview
The **Automated Raw Material Inventory System** is an enterprise-grade DevOps MVP developed across a 15-week curriculum. It manages raw material catalogs, stock-in/stock-out transactions, automated threshold status calculations, search/filtering, and exception alerts. It is packaged with an end-to-end DevOps delivery toolchain spanning Git, Jenkins, Selenium WebDriver, Docker, and Ansible.

---

## Key Viva Questions & Model Answers

### 1. Requirements & System Architecture
* **Q: What is the high-level architecture of your application?**
  * **A:** It is a 3-tier monolithic Spring Boot application running on Java 21:
    - **Presentation Layer:** Server-side Thymeleaf templates with responsive CSS and vanilla JavaScript for dynamic UI interactions.
    - **Service & Business Logic Layer:** Spring MVC REST Controllers (`MaterialController`, `TransactionController`) backed by Spring Services (`MaterialService`, `TransactionService`) that enforce business rules like threshold calculations, atomic stock adjustments, and exception alerts.
    - **Persistence Layer:** Spring Data JPA with an embedded H2 database for local testing and zero-friction CI, extensible to MySQL 8 for production deployments.

* **Q: How does the exception alerting and status logic work?**
  * **A:** Whenever material quantities are updated via stock transactions:
    - If `quantity <= 0`: Status is `OUT_OF_STOCK`.
    - If `0 < quantity <= minimumQuantity`: Status is `LOW_STOCK`.
    - If `quantity > minimumQuantity`: Status is `IN_STOCK`.
    The UI highlights low stock and out-of-stock items using visual badge colors and dedicated operational warning counters.

---

### 2. Git and Branching Strategy
* **Q: Explain your branching policy and release workflow.**
  * **A:** We adopted a GitFlow-inspired branching strategy:
    - `main`: Production-ready baseline, tagged on releases (e.g., `v1.0.0`).
    - `development`: Primary integration branch where verified features are merged.
    - `feature/*`: Short-lived branches created for isolated feature development (e.g., `feature/material-catalog`, `feature/15-week-plan`).
    - Changes require Pull Requests, automated CI checks, and review approval before merge.

* **Q: How do you handle merge conflicts?**
  * **A:** Conflicts typically occur when two concurrent branches modify the same code or configuration files. We resolve conflicts locally on the feature branch by checking out `development`, merging into the feature branch, resolving conflicted markers (`<<<<<<<`, `=======`, `>>>>>>>`), running `./mvnw test` to ensure zero regressions, committing the merge, and pushing to the PR.

---

### 3. Continuous Integration & Jenkins Pipeline
* **Q: What is the structure of your Jenkinsfile?**
  * **A:** We use a declarative Pipeline as Code with the following stages:
    1. **Checkout:** Clones the code from Git SCM.
    2. **Compile and Test:** Runs `./mvnw clean test` (Spring Boot tests + Selenium WebDriver suite).
    3. **Package:** Packages the deployable JAR into `target/`.
    4. **Archive Artifacts:** Stores the JAR and test reports in Jenkins.
    5. **Docker Build:** Builds versioned image `raw-material-inventory:${BUILD_NUMBER}`.
    6. **Push to Registry:** Optionally tags and pushes to Docker Hub/Registry.
    7. **Deploy and Health Check:** Spins up the container and runs an automated 30-attempt HTTP polling loop against `http://localhost:${APP_PORT}/`.
    8. **Post Actions:** Publishes JUnit XML test results and archives failure screenshots via `junit` and `archiveArtifacts`.

* **Q: How does the quality gate work?**
  * **A:** If any unit test or Selenium UI test fails during the `Compile and Test` stage, the Maven build exits with a non-zero code. The pipeline immediately halts, skips packaging and container deployment, marks the build as `FAILED`, and triggers the `post.failure` notification block.

---

### 4. Selenium Automated Testing
* **Q: What user journeys are automated in Selenium?**
  * **A:** 5 critical end-to-end user journeys are automated in `RawMaterialInventorySeleniumTest`:
    1. **TC-01:** Add Material Modal -> Form submission -> Alert confirmation -> Table row verification.
    2. **TC-02:** Dashboard metrics cards verification and keyword search filtering.
    3. **TC-03:** Stock-In movement -> Quantity increment -> Transaction audit history creation.
    4. **TC-04:** Stock-Out movement -> Quantity decrement -> Balance check.
    5. **TC-05:** Negative test case -> Rejecting withdrawal exceeding stock with error "Insufficient stock".

* **Q: How are failure screenshots handled?**
  * **A:** Using a JUnit 5 `TestWatcher` callback extension (`ScreenshotUtil`), which intercepts test execution and takes full-page PNG screenshots via `TakesScreenshot.getScreenshotAs(OutputType.FILE)` into `target/selenium-reports/screenshots/`.

---

### 5. Docker Containerization
* **Q: Explain your Dockerfile design.**
  * **A:** We utilize `eclipse-temurin:21-jre` as the base image because it only bundles the Java Runtime Environment (reducing the final container image size by over 50% compared to a full JDK). The JAR is copied to `/app/app.jar`, port 8080 is exposed, and execution runs through `ENTRYPOINT ["java", "-jar", "/app/app.jar"]`.

* **Q: How do you verify container health and lifecycle?**
  * **A:** Using `docker-compose.yml` and `scripts/docker-lifecycle.sh` which test `run -d`, `docker ps`, `docker logs`, health verification via `curl`, `stop`, `start`, and `rm`.

---

### 6. Ansible Configuration Management
* **Q: What tasks does your Ansible playbook perform?**
  * **A:** `ansible/provision.yml` provisions a target Ubuntu node:
    - Installs `openjdk-21-jre-headless`.
    - Creates a dedicated system user `raw-material-inventory` with `/usr/sbin/nologin`.
    - Creates `/opt/raw-material-inventory` directory with correct permissions.
    - Creates a backup (`app.jar.bak`) of existing artifacts.
    - Deploys the new `app.jar`.
    - Configures and reloads a systemd service unit `/etc/systemd/system/raw-material-inventory.service`.
    - Starts and enables the service on boot.

* **Q: What is idempotency and how do you achieve rollback?**
  * **A:** Idempotency guarantees that executing the playbook multiple times produces the identical desired server state with `changed=0`. Rollback is automated via `ansible/rollback.yml`, which stops the service, restores `app.jar.bak`, restarts the service, and verifies the HTTP 200 health check.
