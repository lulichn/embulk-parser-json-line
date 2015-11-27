# Json Line parser plugin for Embulk

## Overview

* **Plugin type**: parser
* **Guess supported**: no

## Configuration

- **type**: specify this parser as jsonl
- **schema**: specify column name and type (array, required)

## Example

```input_file
{ "columnA": "Lorem ipsum", "columnB": 123456, "columnC": 123.45, "columnD": true,  "columnE": "2000-4-1 12:34:56" }
{ "columnA": "dolor sit amet", "columnB": 789, "columnC": 0.6789, "columnD": false, "columnE": "2015-4-1 00:00:00" }
```

```config_yaml
in:
  type: file  # any file input plugin type
  path_prefix: ./sample.csv
  parser:
    type: jsonl
    schema:
      - { name: columnA, type: string }
      - { name: columnB, type: long }
      - { name: columnC, type: double }
      - { name: columnD, type: boolean }
      - { name: columnE, type: timestamp, format: '%Y-%m-%d %H:%M:%S' }
```

```
$ embulk gem install embulk-parser-json-line
```

## Build

```
$ ./gradlew gem  # -t to watch change of files and rebuild continuously
```
