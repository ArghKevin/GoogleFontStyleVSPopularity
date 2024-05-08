#!/bin/sh

#git pull --recurse
curl -LO https://raw.githubusercontent.com/google/fonts/main/tags/all/families.csv
curl https://fonts.google.com/metadata/stats | \
	# First line is junk.
	sed -n -e '2,$p' > popularity.json
