package ch.carve.maven.consulkv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.ecwid.consul.v1.ConsulClient;

@Mojo(name = "configure", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, threadSafe = true)
public class ConfigureMojo extends AbstractMojo {

    @Parameter(property = "url", required = true)
    private String url;

    @Parameter(property = "prefix", defaultValue = "")
    private String prefix;

    @Parameter(property = "config-dir", defaultValue = "src/main/resources")
    private String configDir;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("start putting config to consul at: " + url);
        Path path = Paths.get(project.getBasedir().toURI().resolve(configDir + "/config.properties"));
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String consulPrefix = prefix == null ? "" : prefix + "/";
        properties.forEach((k, v) -> System.out.println(consulPrefix + k + ":" + v));
        ConsulClient consul = new ConsulClient(url);
        properties.forEach((k, v) -> consul.setKVValue(consulPrefix + (String) k, (String) v));
    }

}
