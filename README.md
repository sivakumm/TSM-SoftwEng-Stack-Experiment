# Stack Experiment

## Commands

### Command to create mosquitto Username and Password

Command on Powershell:

```bash
docker run --rm -v "${PWD}/mosquitto:/tmp_mosquitto" eclipse-mosquitto:2.1.2-alpine mosquitto_passwd -c -b /tmp_mosquitto/passwd USERNAME PASSWORD
```

Linux/Unix:

```bash
docker run --rm -v $(pwd)/mosquitto:/tmp_mosquitto eclipse-mosquitto:2.1.2-alpine mosquitto_passwd -c -b /tmp_mosquitto/passwd USERNAME PASSWORD
```

E. g. to create iot_user with password iot_password

```bash
docker run --rm -v "${PWD}/mosquitto:/tmp_mosquitto" eclipse-mosquitto:2.1.2-alpine mosquitto_passwd -c -b /tmp_mosquitto/passwd iot_user iot_password
```

> Output File: [mosquitto/passwd](./mosquitto/passwd)

