package ch.carve.maven.consulkv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Consumer;

public class ConfigLoader {

    private String baseDir;
    private Consumer<CharSequence> warn;

    public ConfigLoader(String baseDir, Consumer<CharSequence> warn) {
        this.baseDir = baseDir;
        this.warn = warn;
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
                            warn.accept("Failed to load properties from " + p.toString());
                        }
                        allProperties.putAll(props);
                    });
        } catch (IOException e) {
            warn.accept("Failed to walk " + Paths.get(baseDir, folder));
        }
        return allProperties;
    }
}
