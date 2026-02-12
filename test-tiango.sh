#!/bin/bash

# Get the current commit hash
COMMIT_HASH=$(git rev-parse HEAD)

# Get the latest published version for this commit from GitHub releases
# This uses the GitHub API to find the release associated with this commit
get_published_version() {
    local repo="enchf/tiango"
    local commit=$1
    
    # Get all releases and find the one that contains our commit
    local releases=$(gh api repos/$repo/releases --jq '.[].tag_name' 2>/dev/null || echo "")
    
    if [ -n "$releases" ]; then
        # Find the most recent release
        echo "$releases" | head -n1
    else
        # Fallback to local pom.xml version if no releases found
        grep -m1 '<version>' pom.xml | sed 's/.*<version>\(.*\)<\/version>.*/\1/'
    fi
}

PUBLISHED_VERSION=$(get_published_version $COMMIT_HASH)

echo "Commit: $COMMIT_HASH"
echo "Published version: $PUBLISHED_VERSION"

# For now, just echo the version - you can uncomment the actual test later
mvn clean install
java -jar target/tiango-${PUBLISHED_VERSION}.jar -t 30 -d
