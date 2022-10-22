# Dr.Rename

[![Build](https://github.com/drrename/drrename/actions/workflows/build.yml/badge.svg)](https://github.com/drrename/drrename/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=DrRename_drrename&metric=alert_status)](https://sonarcloud.io/dashboard?id=DrRename_drrename)
[![Coverage Status](https://coveralls.io/repos/github/DrRename/drrename/badge.svg)](https://coveralls.io/github/DrRename/drrename)
[![Latest Release](https://img.shields.io/github/release/drrename/drrename.svg)](https://github.com/drrename/drrename/releases/latest)

Dr.Rename is a minimalistic and fast batch-renamer for files and photos. It also comes with "Kodi-Tools", which provide functionality to check the integrity of a [Kodi media library](https://kodi.tv/).

## Prerequirements

You need to have Java **17** installed.

## Installation and Starting

Download [latest Release](https://github.com/drrename/drrename/releases/latest) and call `java -jar drrename-<current-version>-<arch-classifier>.jar`. A double click on the jar-file could also work.

Note that Java needs to be installed.

## Examples

0. Get extension from MIME type. Note that this will also add missing extensions
    ![example-add-missing-extension.png](./screens/example-missing-extension.png)
1. Replace any space by an underscore
    ![example-replace-space-by-underscore.png](./screens/example-replace-space-by-underscore.png)
    
## Kodi Tools

### Examples

## Under the Hood

Dr.Rename is written in Java and uses JavaFX as a UI Toolkit. Furthermore, it uses [Spring Boot](https://spring.io/projects/spring-boot) as the base framework.
