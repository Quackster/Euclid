# Euclid (Java)

A private Habbo Hotel server emulating the ~2007 Shockwave Flash client, ported from C#/.NET to Java.

## Requirements

- Java 17+ (JDK 25 tested)
- Maven 3.9+
- MySQL 8.0+ (or MariaDB)

## Build

```bash
cd euclid-java
mvn clean verify
```

## Database Setup

1. Create the database:
   ```sql
   CREATE DATABASE euclid;
   ```

2. Import the schema:
   ```sql
   USE euclid;
   SOURCE config/euclid.sql;
   ```

3. Configure `config/config.xml` with your MySQL credentials:
   ```xml
   <mysql>
     <hostname>127.0.0.1</hostname>
     <username>root</username>
     <password>yourpassword</password>
     <database>euclid</database>
     <port>3306</port>
     <min_connections>5</min_connections>
     <max_connections>10</max_connections>
   </mysql>
   ```

## Configuration

All configuration is in `config/config.xml`:

| Section | Description |
|---------|-------------|
| `mysql` | Database connection (hostname, port, credentials, pool sizes) |
| `server/main` | Main server IP and port (default `127.0.0.1:37120`) |
| `server/private` | Private server IP and port (default `127.0.0.1:25006`) |
| `server/public` | Public room servers (one `<room>` element per room, with `ip`, `port`, `room_id`) |

Additional config files:
- `config/permissions.yml` — user group permissions (rank-based)
- `config/log4j2.xml` — logging configuration
- `config/public_rooms/` — furniture files for public rooms

## Running

```bash
cd euclid-java
mvn -pl euclid exec:java -Dexec.mainClass=euclid.Euclid
```

Or after building:
```bash
mvn clean package -DskipTests
java -cp "euclid/target/classes:euclid-storage/target/classes:$(mvn -pl euclid dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" euclid.Euclid
```

The server binds:
- **Main** listener (login, room selection)
- **Private** listener (single-room private server)
- **Public** listeners (one per configured public room)

## Plugin API

Place JAR files in the `plugins/` directory. Each plugin must contain a class implementing `euclid.game.plugins.IPlugin`:

```java
public class MyPlugin implements IPlugin {
    @Override
    public void onEnable() {
        // called when the plugin is loaded
    }

    @Override
    public void onDisable() {
        // called when the plugin is unloaded
    }
}
```

Build a plugin JAR:
```bash
cd euclid-plugin-example
mvn package
cp target/euclid-plugin-example-1.0.0.jar ../plugins/
```

## Project Structure

```
euclid-java/
├── pom.xml                  # Parent POM (Java 17, dependencyManagement)
├── euclid-storage/          # Hibernate entities + DAOs
├── euclid/                  # Main server (Netty, game logic, messages)
├── euclid-plugin-example/   # Example plugin
└── config/                  # config.xml, permissions.yml, log4j2.xml, SQL, furniture
```

## Tests

```bash
mvn verify
```

- Unit tests: wire encoding, RC4, config, DAOs, message handlers
- Integration tests: full protocol harness (25 tests) covering login, encryption, room entry, chat, movement, flat management

## C# Reference

The original C# implementation remains in the repository root as the reference implementation. The Java port maintains:
- Byte-for-byte wire protocol compatibility
- Same message semantics and timing
- Same package/class naming convention (class name = wire header)
