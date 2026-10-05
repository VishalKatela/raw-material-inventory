# Configuration Management Script

## Server prerequisites
- Ubuntu host reachable over SSH
- Ansible installed on the control machine
- SSH access and sudo privileges on the target host
- Built application JAR available on the control machine
- Firewall or ingress rule for port 8080

The playbook installs Java 21, creates a dedicated service account and application directory, copies the JAR, and enables a systemd service. It does not install or configure MySQL. The application therefore uses its default in-memory H2 database unless a persistent database configuration is added separately.

## Example Ansible inventory
```yaml
all:
  hosts:
    appserver:
      ansible_host: 192.168.1.20
      ansible_user: ubuntu
      app_dir: /opt/raw-material-inventory
      app_port: 8080
```

## Run provisioning
Copy `ansible/inventory.ini.example` to `ansible/inventory.ini`, replace the example host and user, then run:

```bash
./mvnw package
ansible-playbook -i ansible/inventory.ini ansible/provision.yml \
  -e artifact_path=target/raw-material-inventory-0.0.1-SNAPSHOT.jar
```

After provisioning, check the service with `systemctl status raw-material-inventory` and verify `http://<host>:8080/` returns HTTP 200.

## Provisioning goals
- ensure prerequisites are installed
- create runtime directories
- configure application ownership
- enable and start the systemd service
- configure network access to the required port separately
- keep provisioning repeatable and idempotent
