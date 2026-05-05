# Ether

Image and Graph generator

## Build

```bash
./mvnw clean package
./mvnw jpackage:jpackage -pl cli
```

Then you can install the deb by running:

```bash
apt install cli/target/ether_1.0-SNAPSHOT_amd64.deb
```
