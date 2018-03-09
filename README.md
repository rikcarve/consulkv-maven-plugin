[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ch.carve/consulkv-maven-plugin/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/ch.carve/consulkv-maven-plugin/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# consulkv-maven-plugin
Put application config into consul from your maven build.

Configuration of an application is often tightly coupled with the feature/version of you code. Keeping them together helps a lot in CI/CD pipelines. As there are many ways putting your config into [Consul](https://www.consul.io/) for production, having a maven plugin simplifies sytsem tests a lot. Put your config into consul in the pre-integration test phase, build and start your docker container and voila, your tests work as if they are a live system :-)


## Add plugin
```xml
            <plugin>
                <groupId>ch.carve</groupId>
                <artifactId>consulkv-maven-plugin</artifactId>
                <version>0.1</version>
                <configuration>
                    <url>localhost</url>
                    <configDirs>
                        <configDir>src/main/resources/consul</configDir>
                        <configDir>src/main/resources/consul/dev</configDir>
                    </configDirs>
                    <prefix>hello</prefix>
                </configuration>            
            </plugin>
```
### Configuration
* **url**:        url to consul, e.g. localhost, or 192.168.99.100:8500
* **prefix**:     prefix for the key, e.g. app --> app/key = value
* **configDirs**: one or multiple **configDir** directories with property files

## Link
https://github.com/rikcarve/mp-config-consul

## CLI
There's also a cli version which can be used for staging and production deployment in case maven is not option there. You can build it through:
```bash
mvn package -Pcli
```
### Usage:
```bash
usage: ConsulKvCli
 -configDirs <arg>   comma separated list of directories (absolute)
                     containing property files
 -prefix <arg>       consul key prefix
 -url <arg>          consul url
```

## CLI docker image
In case you can use docker (and I hope you can), there's also a docker image :-)
### Build
```bash
mvn package docker:build -Pcli
```
### Run
```bash
docker run rikcarve/consulkv -configDirs /patch/to/config
```
