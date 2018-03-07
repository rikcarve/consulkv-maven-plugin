package ch.carve.maven.consulkv;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ecwid.consul.v1.ConsulClient;
import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;

public class ConfigureMojoIT {

    private static ConsulProcess consul;

    @BeforeClass
    public static void startConsul() {
        consul = ConsulStarterBuilder.consulStarter().withHttpPort(10001).build().start();
    }

    @AfterClass
    public static void stopConsul() {
        consul.close();
    }

    @Test
    public void testConfigure() throws VerificationException {
        Verifier verifier = new Verifier("src/test/resources/test");
        verifier.setLogFileName("../../../../target/log.txt");
        verifier.executeGoal("consulkv:configure");

        ConsulClient consul = new ConsulClient("localhost:10001");
        String value = consul.getKVValue("hello/hello-prefix").getValue().getDecodedValue();
        Assert.assertEquals("maven say", value);
    }
}
