#!/bin/bash

# Check if two arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <old_text> <new_text>"
    exit 1
fi

# Assign the arguments to variables
old_text="$1"
new_text="$2"

# Find all .txt files in the current directory and its subdirectories, then replace the text
find . -type f -name "*.java" -exec sed -i "s/$old_text/$new_text/g" {} +
