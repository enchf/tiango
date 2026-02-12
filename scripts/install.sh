#!/usr/bin/env bash
set -euo pipefail

# Simple installer for tiango
# Usage: curl -sSL https://raw.githubusercontent.com/enchf/tiango/master/scripts/install.sh | bash -s -- --install-dir "$HOME/.local/bin"

INSTALL_DIR="$HOME/.local/bin"
JAR_DIR="$HOME/.local/lib/tiango"
REPO="enchf/tiango"

# parse args
while [[ "$#" -gt 0 ]]; do
  case "$1" in
    --install-dir) INSTALL_DIR="$2"; shift 2;;
    --jar-dir) JAR_DIR="$2"; shift 2;;
    --repo) REPO="$2"; shift 2;;
    --help) echo "Usage: install.sh [--install-dir DIR] [--jar-dir DIR] [--repo owner/repo]"; exit 0;;
    *) echo "Unknown arg: $1"; exit 1;;
  esac
done

mkdir -p "$INSTALL_DIR"
mkdir -p "$JAR_DIR"

# Get latest release download URL for tiango-*.jar
API_URL="https://api.github.com/repos/$REPO/releases/latest"

echo "Fetching latest release for $REPO..."

# Use curl to fetch release JSON and parse the asset URL for tiango-*.jar
ASSET_URL=$(curl -sSL "$API_URL" | \
  grep "browser_download_url" | \
  grep "tiango-" | \
  head -n1 | sed -E 's/.*"browser_download_url": "([^"]+)".*/\1/')

if [[ -z "$ASSET_URL" ]]; then
  echo "Error: could not find tiango jar in latest release assets." >&2
  exit 1
fi

echo "Found asset: $ASSET_URL"

# Download the jar
JAR_FILENAME=$(basename "$ASSET_URL")
TARGET_JAR="$JAR_DIR/$JAR_FILENAME"

echo "Downloading $JAR_FILENAME to $TARGET_JAR..."

curl -sSL "$ASSET_URL" -o "$TARGET_JAR"
chmod 644 "$TARGET_JAR"

# Remove old jar files
echo "Removing old tiango versions..."
find "$JAR_DIR" -name "tiango-*.jar" -not -name "$JAR_FILENAME" -delete 2>/dev/null || true

# Create wrapper
WRAPPER="$INSTALL_DIR/tiango"
cat > "$WRAPPER" <<'EOF'
#!/usr/bin/env bash
here="$(cd "$(dirname "$0")" && pwd)"
jar="$(ls -1t "$here/../lib/tiango"/*.jar 2>/dev/null | head -n1)"
if [ -z "$jar" ]; then
  echo "No tiango jar found in $here/../lib/tiango" >&2
  exit 1
fi
exec java -jar "$jar" "$@"
EOF

chmod +x "$WRAPPER"

echo "Installed tiango wrapper to $WRAPPER"

# Suggest adding to PATH
echo
if [[ ":$PATH:" != *":$INSTALL_DIR:"* ]]; then
  echo "Add $INSTALL_DIR to your PATH:"
  echo "  export PATH=\"$INSTALL_DIR:\$PATH\"" 
  echo "You can add that line to your ~/.bashrc or ~/.zshrc"
fi

echo "Run 'tiango' to start the app."
