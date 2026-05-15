#!/bin/bash

set -e

LATEST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || echo "v0.0.0")

echo "Latest tag: $LATEST_TAG"

VERSION=${LATEST_TAG#v}
IFS='.' read -r MAJOR MINOR PATCH <<< "$VERSION"

PATCH=$((PATCH + 1))
NEW_TAG="v$MAJOR.$MINOR.$PATCH"

echo "New tag: $NEW_TAG"

# 🔥 Prevent duplicate tag creation
if git rev-parse "$NEW_TAG" >/dev/null 2>&1; then
  echo "Tag $NEW_TAG already exists locally. Exiting."
  exit 1
fi

# Also check remote
if git ls-remote --tags origin | grep -q "refs/tags/$NEW_TAG"; then
  echo "Tag $NEW_TAG already exists on remote. Exiting."
  exit 1
fi

# Commit if needed
if git diff --quiet && git diff --cached --quiet; then
  echo "No changes to commit"
else
  git add -A
  git commit -m "Release $NEW_TAG"
  git push origin main
fi

# Create + push tag
git tag "$NEW_TAG"
git push origin "$NEW_TAG"

echo "Released $NEW_TAG 🚀"