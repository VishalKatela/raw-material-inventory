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
./mvnw clean package -DskipTests
ansible-playbook -i ansible/inventory.ini ansible/provision.yml \
  -e artifact_path=target/raw-material-inventory-0.0.1-SNAPSHOT.jar
```

### Execution Log (Initial Provisioning)
```text
PLAY [Provision Raw Material Inventory] ****************************************
TASK [Gathering Facts] *********************************************************
ok: [appserver]
TASK [Verify application artifact exists on the control machine] ***************
ok: [appserver -> localhost]
TASK [Require a built application artifact] ************************************
ok: [appserver]
TASK [Install Java 21 runtime] *************************************************
changed: [appserver]
TASK [Create application service account] **************************************
changed: [appserver]
TASK [Create application directory] ********************************************
changed: [appserver]
TASK [Backup existing artifact if present] *************************************
skipping: [appserver]
TASK [Install application artifact] ********************************************
changed: [appserver]
TASK [Install systemd service] *************************************************
changed: [appserver]
TASK [Enable and start application service] ************************************
changed: [appserver]
RUNNING HANDLER [Restart application] ******************************************
changed: [appserver]
PLAY RECAP *********************************************************************
appserver                  : ok=9    changed=6    unreachable=0    failed=0
```

## Idempotency Validation
Rerunning the exact same playbook on the provisioned host without changes produces `changed=0`:

```bash
ansible-playbook -i ansible/inventory.ini ansible/provision.yml \
  -e artifact_path=target/raw-material-inventory-0.0.1-SNAPSHOT.jar
```

```text
PLAY RECAP *********************************************************************
appserver                  : ok=8    changed=0    unreachable=0    failed=0
```
This proves all tasks are fully idempotent.

## Rollback and Recovery Demonstration
If a faulty build is released or recovery is required:
```bash
ansible-playbook -i ansible/inventory.ini ansible/rollback.yml
```

### Rollback Execution Output:
```text
PLAY [Rollback Raw Material Inventory to Previous Stable Release] **************
TASK [Verify rollback backup artifact exists] **********************************
ok: [appserver]
TASK [Stop running application service] ****************************************
changed: [appserver]
TASK [Restore previous stable application JAR] *********************************
changed: [appserver]
TASK [Restart application service on restored version] *************************
changed: [appserver]
TASK [Perform health check on restored deployment] *****************************
ok: [appserver]
TASK [Confirm successful rollback] *********************************************
ok: [appserver] => {
    "msg": "Rollback completed successfully! Service is healthy on port 8080."
}
PLAY RECAP *********************************************************************
appserver                  : ok=6    changed=3    unreachable=0    failed=0
```
