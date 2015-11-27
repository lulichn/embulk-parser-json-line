# Json Line parser plugin for Embulk
This plugin has rewritten of [shun0102 / embulk-parser-jsonl Plugin](https://github.com/shun0102/embulk-parser-jsonl) using Java.

## Overview

* **Plugin type**: parser
* **Guess supported**: no

## Configuration

- **type**: specify this parser as jsonl
- **schema**: specify column name and type (array, required)

## Example
Input Data

```json:
{ "columnA": "Lorem ipsum", "columnB": 123456, "columnC": 123.45, "columnD": true,  "columnE": "2000-4-1 12:34:56" }
{ "columnA": "dolor sit amet", "columnB": 789, "columnC": 0.6789, "columnD": false, "columnE": "2015-4-1 00:00:00" }
```

Config

```yaml:
in:
  type: file  # any file input plugin type
  path_prefix: ./sample.txt
  parser:
    type: jsonl
    schema:
      - { name: columnA, type: string }
      - { name: columnB, type: long }
      - { name: columnC, type: double }
      - { name: columnD, type: boolean }
      - { name: columnE, type: timestamp, format: '%Y-%m-%d %H:%M:%S' }
out:
  type: stdout
```
Embulk Preview

```bash:
$ embulk preview sample.yml
```

Preview Result

| columnA:string | columnB:long | columnC:double | columnD:boolean |       columnE:timestamp |
|---------------:|-------------:|---------------:|----------------:|------------------------:|
| Lorem ipsum    |      123,456 |         123.45 |            true | 2000-04-01 12:34:56 UTC |
| dolor sit amet |          789 |         0.6789 |           false | 2015-04-01 00:00:00 UTC |


## Installation
TODO: Write

<!-- 
Not registry rubygems repository.
```
$ embulk gem install embulk-parser-json-line
```
-->

## Build

```
$ ./gradlew gem
```
