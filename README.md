# Palikka [![Build Status](https://travis-ci.org/metosin/palikka.svg?branch=master)](https://travis-ci.org/metosin/palikka)

[![Clojars Project](http://clojars.org/metosin/palikka/latest-version.svg)](http://clojars.org/metosin/palikka)

[API Docs](http://metosin.github.io/palikka/palikka.core.html).

## Features

- Integrated with [maailma](https://github.com/metosin/maailma) envinronment lib, provides Schemas etc.
    - **Problem**: Currently each component reads their config from static path in the config map, thus making in impossible to have multiple instances of the same component with different config
- [palikka.test-utils](./src/palikka/test_utils.clj) provides fixtures for `clojure.test` and background for Midje.
- Context. Context is a selected set of stuff from system. Passing the whole system down to implementation is inconvenient as the interesting bits of system are somewhere inside the component. Using context e.g. mongo component can decide to publish `:conn`, `:db` and `:gfs` as top-level context keys.
    - **Problem**: As context keys are hardcoded in component, multiple instances of the same component would shadow others context keys.

## Component dependencies

This library doesn't depend on libraries used by components,
depend on these on your project.

- Http-kit: [http-kit](https://github.com/http-kit/http-kit)
- Mongo: [monger](https://github.com/michaelklishin/monger)
- Database: [hikari-cp](https://github.com/tomekw/hikari-cp) & jdbc driver for used db, e.g. [postgres driver](http://mvnrepository.com/artifact/org.postgresql/postgresql)
- Migration status checker: [flyway-core](http://mvnrepository.com/artifact/com.googlecode.flyway/flyway-core)

## Todo

- [ ] Jetty
- [ ] Logging component?

## License

Copyright © 2015 [Metosin Oy](http://www.metosin.fi)

Distributed under the Eclipse Public License, the same as Clojure.
