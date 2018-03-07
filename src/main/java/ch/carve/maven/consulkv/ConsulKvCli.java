package ch.carve.maven.consulkv;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.ecwid.consul.v1.ConsulClient;

public class ConsulKvCli {

    private static Options options = new Options();

    public static void main(String[] args) {
        CommandLine cmd = parseCommandLine(args);

        if (!cmd.hasOption("configDirs")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ConsulKvCli", options);
            System.exit(1);
        }
        String configDirs = cmd.getOptionValue("configDirs");
        String url = cmd.getOptionValue("url", "localhost");
        String prefix = cmd.getOptionValue("prefix");

        ConfigLoader loader = new ConfigLoader("", ConsulKvCli::log);
        ConsulClient consul = new ConsulClient(url);

        List<String> configDirList = Arrays.asList(configDirs.split(","));
        configDirList.forEach(configDir -> {
            Properties properties = loader.loadProperties(configDir);
            ConfigureMojo.configureConsul(consul, properties, prefix, ConsulKvCli::log);
        });

    }

    private static CommandLine parseCommandLine(String[] args) {
        options.addOption(new Option("configDirs", true, "comma separated list of directories (absolute) containing property files"));
        options.addOption(new Option("url", true, "consul url"));
        options.addOption(new Option("prefix", true, "consul key prefix"));
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ConsulKvCli", options);
        }
        return cmd;
    }

    private static void log(CharSequence bla) {
        System.out.println(bla);
    }

}
