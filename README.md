# Palikka [![Build Status](https://travis-ci.org/metosin/palikka.svg?branch=master)](https://travis-ci.org/metosin/palikka)

[![Clojars Project](http://clojars.org/metosin/palikka/latest-version.svg)](http://clojars.org/metosin/palikka)

[API Docs](http://metosin.github.io/palikka/palikka.core.html).

## Project statement

Unlike more general of our libraries (like
[compojure-api](https://github.com/metosin/compojure-api) and
[ring-swagger](https://github.com/metosin/ring-swagger)) this project is
primarily intended for use in Metosin's projects. Feel free to use, but
don't expect full support.

- We might remove features if we think they are not useful anymore
- We will reject PRs and issues about features we wouldn't use ourselves

## Features

- The components use [clojure.tools.logging](https://github.com/clojure/tools.logging)
- The components use Schema to validate and coerce their configuration
    - Coercion allows using strings from environment variables and system
    properties easily
- Includes [test utilities](./src/palikka/test_utils.clj) for `clojure.test` and Midje.
- **Context** is curated value created from interesting parts of the system.
    - Passing the whole system down to implementation is inconvenient as the
    interesting bits of system are somewhere inside the components.
    - Follows the semantics of using
    - `(providing (mongo/create (:db env)) [:db])` will publish value of `(:db mongo)` as `(:db context)`
    - `(providing (mongo/create (:db env)) {:mongo :db})` will publish value of `(:db mongo)` as `(:mongo context)`
    - `(providing (mongo/create (:db env)) {:mongo (fn [component] (:db component))})` will publish value of `(:db mongo)` as `(:mongo context)`
- Ring handler can be created using a function taking `system` as argument. This makes it easy to create handler accessing system or context, directly or through middleware.
    - `(http-kit/create (:http env) {:fn (fn [system] (let [ctx (palikka.core/create-context system)] (fn [req] {:body ctx})))})`
- [Idempotent components](https://github.com/stuartsierra/component#idempotence)

## Differences to [System](https://github.com/danielsz/system)

- Components log messages
- **Context**
- Schema validation and coercion for options

## Example

```clj
(defn base-system [override]
  (let [env (m/build-config
              (m/resource "config-defaults.edn")
              (m/file "./config-local.edn"))
        create-handler (fn [system]
                         (-> (create-handler system)
                             (wrap-context system)))]
    (component/system-map
      :mongo        (-> (mongo/create (:mongo env))
                        (providing {:db :db, :gfs :gfs}))
      :http         (-> (http-kit/create (:http env) {:fn create-handler})
                        ; Though components are accessed through context,
                        ; complete system map is needed for creating the context
                        (using [:mongo]))
      :nrepl        (nrepl/create (:nrepl env)))))
```

## Component dependencies

This library doesn't depend on libraries used by components,
depend on these on your project.

- Http-kit: [http-kit](https://github.com/http-kit/http-kit)
- Mongo: [monger](https://github.com/michaelklishin/monger)
- Database: [hikari-cp](https://github.com/tomekw/hikari-cp) & jdbc driver for used db, e.g. [postgres driver](http://mvnrepository.com/artifact/org.postgresql/postgresql)
- Migration status checker: [flyway-core](http://mvnrepository.com/artifact/com.googlecode.flyway/flyway-core)
- RabbitMQ: [Langohr](https://github.com/michaelklishin/langohr)
- Datomic: `[com.datomic/datomic-free "0.9.5327"]` or Datomic Pro

## License

Copyright © 2015-2016 [Metosin Oy](http://www.metosin.fi)

Distributed under the Eclipse Public License, the same as Clojure.
