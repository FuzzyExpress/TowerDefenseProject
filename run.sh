#!/bin/bash
set -e

# make sure to not blow up wrong files with rm
cd ~/Documents/Java/TowerDefenseProject

# Create output directory if it doesn't exist
mkdir -p out

# Clean previous build
rm -rf out/*

# Compile all Java files
echo "Compiling Java files..."
find src -name "*.java" | xargs javac -d out

# Run the application
echo "Running MainGUI..."
java -cp out MainGUI