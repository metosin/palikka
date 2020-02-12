## 0.7.1 (2020-02-12)

- Fix http-kit log message

## 0.7.0 (2020-02-12)

- Support all Flyway options
    - Flyway clojure options map is now converted dynamically to configuration
    builder calls, so any option should be supported without changes to the
    wrapper code.
- Add Immutant-HTTP component

## 0.6.1 (2018-11-21)

- Fix Flyway component
- Add Flyway `table` optin

## 0.6.0 (2018-11-21)

- Update deps
- Use `[nrepl "0.4.5"]`
- Remove `palikka.sweet` namespace

## 0.5.4 (2018-01-26)

- Stored context in a record and implemented a print-method for the record,
which hides the contents to prevent accidentally exposing possible secrets
on the context map.

## 0.5.3 (27.10.2017)

- Update deps
- Allow all nrepl options, default `:bind` to `localhost`

## 0.5.2 (28.9.2016)

- Don't throw Flyway exceptions by default
- Update deps
- Fix Datomic component

## 0.5.1 (19.5.2016)

- Fixed Flyway component error handling and logging

## 0.5.0 (13.4.2016)

- Allow use of database component without port-number (with jdbc-url)
- Added tests for the database component

## 0.4.0 (23.3.2015)

- Add Aleph component
- Catch exceptions in stop methods as [recommended in Component README](https://github.com/stuartsierra/component/#idempotence)
- Prefixed Component record properties with `component-` so that it is more unlikely that these will collide with dependency components
    - `config` component used to cause problem because it overrode `config` property

## 0.3.0 (7.11.2015)
