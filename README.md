# Palikka [![Build Status](https://travis-ci.org/metosin/palikka.svg?branch=master)](https://travis-ci.org/metosin/palikka)

[![Clojars Project](http://clojars.org/metosin/palikka/latest-version.svg)](http://clojars.org/metosin/palikka)

[API Docs](http://metosin.github.io/palikka/palikka.core.html).

## Features

- The components use [clojure.tools.logging](https://github.com/clojure/tools.logging)
- Includes [test utilities](./src/palikka/test_utils.clj) for `clojure.test` and Midje.
- **Context** is curated value created from interesting parts of the system.
    - Passing the whole system down to implementation is inconvenient as the
    interesting bits of system are somewhere inside the components.
    - Follows the semantics of using
    - `(providing (mongo/create (:db env)) [:db])` will publish value of `(:db mongo)` as `(:db context)`
    - `(providing (mongo/create (:db env)) {:mongo :db})` will publish value of `(:db mongo)` as `(:mongo context)`
    - `(providing (mongo/create (:db env)) {:mongo (fn [component] (:db component))})` will publish value of `(:db mongo)` as `(:mongo context)`

## Differences to [System](https://github.com/danielsz/system)

- Components log messages
- **Context**
- Schema validation for options

## Component dependencies

This library doesn't depend on libraries used by components,
depend on these on your project.

- Http-kit: [http-kit](https://github.com/http-kit/http-kit)
- Mongo: [monger](https://github.com/michaelklishin/monger)
- Database: [hikari-cp](https://github.com/tomekw/hikari-cp) & jdbc driver for used db, e.g. [postgres driver](http://mvnrepository.com/artifact/org.postgresql/postgresql)
- Migration status checker: [flyway-core](http://mvnrepository.com/artifact/com.googlecode.flyway/flyway-core)
- RabbitMQ: [Langohr](https://github.com/michaelklishin/langohr)
- Datomic: `[com.datomic/datomic-free "0.9.5327"]` or Datomic Pro

## Todo

- [ ] Jetty

## License

Copyright © 2015 [Metosin Oy](http://www.metosin.fi)

Distributed under the Eclipse Public License, the same as Clojure.
