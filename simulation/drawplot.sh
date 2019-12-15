#!/bin/bash

python pretty.py | sort -n > pretty.csv
python plot.py

