#!/bin/bash

original_image="info.png"

mkdir -p "res/drawable-mdpi"
mkdir -p "res/drawable-hdpi"
mkdir -p "res/drawable-xhdpi"
mkdir -p "res/drawable-xxhdpi"

convert "$original_image" -resize 50% "res/drawable-mdpi/ic_info.png"
convert "$original_image" -resize 75% "res/drawable-hdpi/ic_info.png"
convert "$original_image" -resize 100% "res/drawable-xhdpi/ic_info.png"
convert "$original_image" -resize 150% "res/drawable-xxhdpi/ic_info.png"

echo "Drawable resources created."
