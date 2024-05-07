#!/bin/sh

find ../fonts/ -type f -name METADATA.pb | while read -r line
do
	o=$(echo $line | sed -E -e 's|.*/(.*)/METADATA.pb|\1.pb|g')
	mv $line $o
done
