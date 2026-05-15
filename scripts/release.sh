#!/bin/bash

set -e

# Get latest tag (fallback to v0.0.0 if none exist)
LATEST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "v0.0.0")

echo "Latest tag: $LATEST_TAG"

# Strip "v"
VERSION=${LATEST_TAG#v}

IFS='.' read -r MAJOR MINOR PATCH <<< "$VERSION"

# Increment patch version
PATCH=$((PATCH + 1))

NEW_TAG="v$MAJOR.$MINOR.$PATCH"

echo "New tag: $NEW_TAG"

# Commit any changes (optional safety)
git add -A
git commit -m "Release $NEW_TAG" || echo "No changes to commit"

# Create new tag
git tag "$NEW_TAG"

# Push commit + tag
git push origin main
git push origin "$NEW_TAG"

echo "Released $NEW_TAG 🚀"