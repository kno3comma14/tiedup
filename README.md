# TiedUp

Basic game to show a PoC of CLJS, Rust and WASM integration.

## Setup

To get an interactive development environment run:


    lein figwheel dev

and open your browser at [localhost:3449](http://localhost:3449/).

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 
