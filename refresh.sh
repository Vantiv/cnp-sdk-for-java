#!/bin/bash
# remove old generated classes and perform a Gradle build
rm -f build/generated/io/github/vantiv/sdk/generate/*.java && gradle cleanEclipse eclipse

