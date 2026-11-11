#!/usr/bin/env sh
set -eu

GRADLE_VERSION=9.6.0
TMP_DIR="${TMPDIR:-/tmp}/lec10-gradle-bootstrap-$$"
ZIP="$TMP_DIR/gradle.zip"
URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"

if [ -f ./gradlew ]; then
  echo "Gradle Wrapper already exists."
  exit 0
fi

mkdir -p "$TMP_DIR"

if command -v curl >/dev/null 2>&1; then
  curl -L "$URL" -o "$ZIP"
elif command -v wget >/dev/null 2>&1; then
  wget "$URL" -O "$ZIP"
else
  echo "curl or wget is required" >&2
  exit 1
fi

unzip -q "$ZIP" -d "$TMP_DIR"
"$TMP_DIR/gradle-${GRADLE_VERSION}/bin/gradle" wrapper --gradle-version "$GRADLE_VERSION" --distribution-type bin
rm -rf "$TMP_DIR"
chmod +x gradlew

echo "Gradle Wrapper created successfully."
