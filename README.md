# consulkv-maven-plugin
Put application config into consul from your maven build.

Configuration of an application is often tightly coupled with the feature/version of you code. Keeping them together helps a lot in CI/CD pipelines. As there are many ways putting your config into [Consul](https://www.consul.io/) for production, having a maven plugin simplifies sytsem tests a lot. Put your config into consul in the pre-integration test phase, build and start your docker container and voila, your tests work as if they are a live system :-)


## Add plugin
```xml
            <plugin>
                <groupId>ch.carve</groupId>
                <artifactId>consulkv-maven-plugin</artifactId>
                <version>0.1-SNAPSHOT</version>
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
