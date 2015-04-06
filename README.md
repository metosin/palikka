# Palikka [![Build Status](https://travis-ci.org/metosin/palikka.svg?branch=master)](https://travis-ci.org/metosin/palikka)

[![Clojars Project](http://clojars.org/metosin/palikka/latest-version.svg)](http://clojars.org/metosin/palikka)

[API Docs](http://metosin.github.io/palikka/palikka.core.html).

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
