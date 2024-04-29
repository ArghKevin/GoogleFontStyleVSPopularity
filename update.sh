#!/bin/sh

git pull --recurse
curl https://fonts.google.com/metadata/stats | \
	# First line is junk.
	sed -n -e '2,$p' > popularity.json
