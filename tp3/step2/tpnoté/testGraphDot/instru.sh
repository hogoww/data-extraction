#!/bin/bash

dot -Tpng graphGenerated.dot -o callgraph.png
xdg-open callgraph.png
