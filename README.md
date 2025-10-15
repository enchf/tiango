# tiango
A small monkey that helps you to keep your screen on.

## Requirements
- Java 21 or newer
- Maven

## Build
Compile and package the project using Maven:

```sh
mvn clean package
```

## Run
You can download the latest release from the [Releases page](https://github.com/enchf/tiango/releases/latest).

## Installation (one-line)

Install the latest release and an executable wrapper with a single command:

```sh
curl -sSL https://raw.githubusercontent.com/enchf/tiango/master/scripts/install.sh | bash -s -- --install-dir "$HOME/.local/bin"
```

This will:
- Download the latest `tiango-<version>.jar` into `~/.local/lib/tiango/`
- Install a runnable wrapper `~/.local/bin/tiango` that executes `java -jar` on the installed JAR

If you prefer an alias instead of a wrapper, add to your `~/.bashrc` or `~/.zshrc`:

```sh
alias tiango='java -jar ~/.local/lib/tiango/tiango-<version>.jar'
```

Make sure `~/.local/bin` is in your `PATH` (the installer will show instructions if it isn't).

## Contributing

This project follows [Semantic Versioning](https://semver.org/) and uses automated releases. To contribute, please follow these commit message conventions:

### Commit Message Format
Each commit message consists of a **type**, a **subject**, and optionally a **body** and **footer**:

```
<type>(<scope>): <subject>

<body>

<footer>
```

#### Types
- `feat`: A new feature (minor version bump)
- `fix`: A bug fix (patch version bump)
- `docs`: Documentation only changes
- `style`: Changes that do not affect the meaning of the code
- `refactor`: A code change that neither fixes a bug nor adds a feature
- `perf`: A code change that improves performance
- `test`: Adding missing tests or correcting existing tests
- `chore`: Changes to the build process or auxiliary tools

#### Breaking Changes
To create a breaking change (major version bump), add `BREAKING CHANGE:` in the commit message body or append a `!` after the type:

```
feat!: change command line interface

BREAKING CHANGE: The --timeout flag is now required
```

Examples:
```bash
git commit -m "fix: prevent screen from sleeping"
git commit -m "feat: add new animation pattern"
git commit -m "docs: update installation instructions"
```

After building, you can run the executable JAR:

```sh
java -jar target/snippets-1.0-SNAPSHOT.jar
```

You can also pass ah argument to specify the number of iterations:

```sh
java -jar target/snippets-1.0-SNAPSHOT.jar <iterations>
```
