#!/usr/bin/env bash
set -euo pipefail

VERSION="$1"
shift || true

echo "Tagging $VERSION"

if git rev-parse --verify "$VERSION" >/dev/null 2>&1; then
  git tag -d "$VERSION"
  git push --delete origin "$VERSION" || true
fi

git tag -a "$VERSION" -m "Release $VERSION"
git push origin "$VERSION"

echo "Done (GitHub Actions will handle build + release)"