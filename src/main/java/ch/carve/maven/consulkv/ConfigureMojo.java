package ch.carve.maven.consulkv;

import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.ecwid.consul.v1.ConsulClient;

/**
 * consulkv:configure goal
 * 
 * @author Arik Dasen
 *
 */
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
        ConfigLoader loader = new ConfigLoader(project.getBasedir().getAbsolutePath(), getLog()::warn);
        ConsulClient consul = new ConsulClient(url);
        configDirs.forEach(configDir -> {
            Properties properties = loader.loadProperties(configDir);
            configureConsul(consul, properties, prefix, getLog()::info);
        });
    }

    /**
     * Put key value pairs specified as properties into consul with a prefix
     * 
     * @param consul
     * @param properties
     * @param prefix
     * @param log
     */
    public static void configureConsul(ConsulClient consul, Properties properties, String prefix, Consumer<CharSequence> log) {
        String consulPrefix = prefix == null ? "" : prefix + "/";
        properties.forEach((k, v) -> {
            consul.setKVValue(consulPrefix + k, (String) v);
            log.accept("Put " + consulPrefix + k + ":" + v);
        });
    }

}
