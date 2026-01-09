Gradle usage

Build:

```bash
./gradlew build
```

Run tests:

```bash
./gradlew test
```

Run the game (development):

```bash
./gradlew runGame
# or
./gradlew run
```

Create runnable jar:

```bash
./gradlew jar
java -jar build/libs/Donkey-Kong-Country-1.0-SNAPSHOT.jar
```

Notes:
- The project uses Java 11 compatibility.
- Tests are executed automatically with `./gradlew build`, but some tests currently fail (see build report at `build/reports/tests/test/index.html`).
