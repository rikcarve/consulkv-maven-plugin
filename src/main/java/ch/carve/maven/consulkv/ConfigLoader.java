package ch.carve.maven.consulkv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.maven.plugin.logging.Log;

public class ConfigLoader {

    private String baseDir;
    private Log log;

    public ConfigLoader(String baseDir, Log log) {
        this.baseDir = baseDir;
        this.log = log;
    }

    public Properties loadProperties(String folder) {
        Properties allProperties = new Properties();
        try {
            Files.list(Paths.get(baseDir, folder))
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        Properties props = new Properties();
                        try {
                            props.load(Files.newInputStream(p));
                        } catch (IOException e) {
                            log.warn("Failed to load properties from " + p.toString());
                        }
                        allProperties.putAll(props);
                    });
        } catch (IOException e) {
            log.warn("Failed to walk " + Paths.get(baseDir, folder));
        }
        return allProperties;
    }
}
