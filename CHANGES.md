## Unreleased

## 0.4.0 (23.3.2015)

- Add Aleph component
- Catch exceptions in stop methods as [recommended in Component README](https://github.com/stuartsierra/component/#idempotence)
- Prefixed Component record properties with `component-` so that it is more unlikely that these will collide with dependency components
    - `config` component used to cause problem because it overrode `config` property

## 0.3.0 (7.11.2015)
