#!/bin/sh

set -ex

# Compile our wasm module and run `wasm-bindgen`
wasm-pack build --target nodejs

# Run the `wasm2js` tool from `binaryen`
wasm2js pkg/lib_bg.wasm -o resources/public/js/fileloader.js