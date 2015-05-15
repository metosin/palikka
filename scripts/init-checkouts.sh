#!/bin/bash

mkdir -p checkouts
cd checkouts

if [[ -d maailma ]]; then
    git pull
else
    git clone git@github.com:metosin/maailma.git
fi

cd maailma
lein install
