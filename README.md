# om-tut2

Almost Intermediate Om Tutorial updated for Om 0.7.0

This repo is simply a reprise of the Basic Om tutorial
updated with the following changes:
* The ```editable``` Om component has been modified to handle
  classes as cursors (and avoid the IClonable hacks).
* The ```index.html``` has been modified to specify the document type,
  language and character encoding. The React JavaScript library
  is accessed locally (vs. checking with Facebook each time it is loaded).
* Uses ```goog.dom``` as with the Intermediate tutorial

## Running

Download the React javascript library:
    wget http://fb.me/react-0.11.1.js

Have [Leiningen](https://github.com/technomancy/leiningen) auto build ClojureScript sources:
    lein cljsbuild auto om-tut2

Open ```index.html``` in your favorite browser:
    open index.html

## References

* David Nolen's [Om](https://github.com/swannodette/om) - A
  [ClojureScript](http://github.com/clojure/clojurescript) interface
  to [Facebook's React](http://facebook.github.io/react/).
* The Om [Basic](https://github.com/swannodette/om/wiki/Basic-Tutorial) tutorial
* The Om [Intermediate](https://github.com/swannodette/om/wiki/Intermediate-Tutorial) tutorial

## Copyright and license

Copyright © 2014 Tom Marble

Licensed under the EPL (see the file [EPL](https://raw.githubusercontent.com/tmarble/om-tut2/master/EPL)).
