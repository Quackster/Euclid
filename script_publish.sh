#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "Building Euclid (Java)..."
mvn clean package -DskipTests -q

echo "Creating release bundle..."
RELEASE_DIR="dist/Euclid"
rm -rf dist
mkdir -p "$RELEASE_DIR"

# Copy JARs
cp euclid/target/euclid-1.0.0.jar "$RELEASE_DIR/"
cp euclid-storage/target/euclid-storage-1.0.0.jar "$RELEASE_DIR/"

# Copy dependencies
mkdir -p "$RELEASE_DIR/lib"
mvn -pl euclid dependency:copy-dependencies -DoutputDirectory="$RELEASE_DIR/lib" -q

# Copy config
mkdir -p "$RELEASE_DIR/config"
cp config/config.xml config/permissions.yml config/log4j2.xml "$RELEASE_DIR/config/"
cp -r config/public_rooms "$RELEASE_DIR/config/"
cp config/euclid.sql "$RELEASE_DIR/config/"

# Create plugins directory
mkdir -p "$RELEASE_DIR/plugins"

# Create run script
cat > "$RELEASE_DIR/run.sh" << 'EOF'
#!/bin/bash
DIR="$(cd "$(dirname "$0")" && pwd)"
java -cp "euclid-1.0.0.jar:euclid-storage-1.0.0.jar:lib/*" euclid.Euclid
EOF
chmod +x "$RELEASE_DIR/run.sh"

# Create Windows run script
cat > "$RELEASE_DIR/run.bat" << 'EOF'
@echo off
set DIR=%~dp0
java -cp "euclid-1.0.0.jar;euclid-storage-1.0.0.jar;lib\*" euclid.Euclid
EOF

echo "Done! Release bundle at: $RELEASE_DIR"
echo "Run with: $RELEASE_DIR/run.sh"
