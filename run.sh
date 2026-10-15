#!/usr/bin/env bash
# Kasutus:
#   ./run.sh         kompileerib ja käivitab rakenduse
#   ./run.sh test    jooksutab kõik testid (vajab Mavenit)
set -euo pipefail

cd "$(dirname "$0")"

# macOS-is on /usr/bin/java olemas ka siis, kui Javat pole, seega kontrollime, et see päriselt töötab
if ! java -version >/dev/null 2>&1; then
    for dir in /opt/homebrew/opt/openjdk@21 /opt/homebrew/opt/openjdk /usr/local/opt/openjdk@21 /usr/local/opt/openjdk; do
        if [ -x "$dir/bin/java" ]; then
            export PATH="$dir/bin:$PATH"
            break
        fi
    done
fi

if ! java -version >/dev/null 2>&1; then
    echo "Javat ei leitud. Paigalda see käsuga: brew install openjdk@21" >&2
    exit 1
fi

if [ "${1:-}" = "test" ]; then
    exec mvn test
fi

mkdir -p target/classes
javac --release 21 -encoding UTF-8 -d target/classes src/main/java/laohaldus/*.java
exec java -cp target/classes laohaldus.Main
