package ch.carve.maven.consulkv;

import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.ecwid.consul.v1.ConsulClient;

@Mojo(name = "configure", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST, threadSafe = true)
public class ConfigureMojo extends AbstractMojo {

    /**
     * Consul url in the format of "localhost:8500" or "localhost" (with default
     * port 8500)
     */
    @Parameter(property = "url", required = true)
    private String url;

    /**
     * Prefix used in consul key/value tree, e.g. "prefix/key = vakue"
     */
    @Parameter(property = "prefix", defaultValue = "")
    private String prefix;

    /**
     * A list of directories which will be scanned for property files
     */
    @Parameter(property = "config-dirs", defaultValue = "src/main/resources")
    private List<String> configDirs;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("start putting config to consul at: " + url);
        ConfigLoader loader = new ConfigLoader(project.getBasedir().getAbsolutePath(), getLog());
        configDirs.forEach(configDir -> {
            Properties properties = loader.loadProperties(configDir);
            String consulPrefix = prefix == null ? "" : prefix + "/";
            ConsulClient consul = new ConsulClient(url);
            properties.forEach((k, v) -> {
                consul.setKVValue(consulPrefix + k, (String) v);
                getLog().info("Put " + consulPrefix + k + ":" + v);
            });
        });
    }

}
