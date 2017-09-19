#!/bin/bash
# remove old generated classes and perform a Gradle build
rm -f build/generated/com/cnp/sdk/generate/*.java && gradle cleanEclipse eclipse

