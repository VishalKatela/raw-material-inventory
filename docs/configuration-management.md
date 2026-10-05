# Configuration Management Script

## Server prerequisites
- Java 21 runtime
- Maven or wrapper
- MySQL 8 server
- Working directory for application deployment
- User account for service runtime
- Firewall or ingress rule for port 8080
- Systemd service file or Tomcat deployment folder

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

## Example Ansible playbook
```yaml
- hosts: appserver
  become: true
  tasks:
    - name: Install Java
      apt:
        name: openjdk-21-jre-headless
        state: present

    - name: Install MySQL client
      apt:
        name: mysql-client
        state: present

    - name: Create application directory
      file:
        path: /opt/raw-material-inventory
        state: directory
        mode: '0755'
```

## Provisioning goals
- ensure prerequisites are installed
- create runtime directories
- configure application ownership
- expose the required port
- ensure repeatable and idempotent setup
