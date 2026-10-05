# Final End-to-End Release, Documentation and Viva

## Release checklist
- [x] repository is initialized and documented
- [ ] source code is committed and reviewed
- [x] GitHub Actions and Jenkins build pipelines are defined
- [x] automated Maven tests are included in CI
- [ ] Docker image is built by a Docker-enabled Jenkins agent
- [x] configuration management playbook and deployment notes are included
- [ ] release baseline/tag is confirmed in the current checkout

## Architecture summary
The project is a Java-based inventory system using Spring Boot, Spring Data JPA, embedded Tomcat, and a Thymeleaf dashboard. The checked-in default configuration uses an in-memory H2 database for local development and runtime validation. MySQL remains the intended persistent deployment database, but production database configuration and provisioning are not yet part of this release.

## Troubleshooting guide
- Check Java 21 compatibility.
- Confirm port 8080 is free, or set `server.port` to another available port.
- For the current default setup, verify the H2 datasource configuration.
- For persistent deployment, configure and verify MySQL before enabling it; the current app does not include a MySQL production profile.
- Inspect application logs for startup errors and confirm the HTTP endpoint responds.

## Limitations
- the MVP is intentionally small and operationally focused
- the default H2 database is in-memory; data does not survive application restart
- persistent MySQL deployment configuration remains future work
- reporting is minimal by design
- authentication and authorization are not part of the MVP
- deployment is basic and intended for local or small environment use

## Future enhancements
- role-based access control
- advanced reports and analytics
- notification engine
- barcode/RFID integration
- cloud deployment support

## Local verification
The application was started with `./mvnw spring-boot:run` on Java 21. The dashboard returned HTTP 200 from `http://localhost:8080`. Maven tests should be rerun in CI for the release commit.
