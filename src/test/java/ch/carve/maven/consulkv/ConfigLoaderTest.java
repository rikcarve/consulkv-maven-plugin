package ch.carve.maven.consulkv;

import java.util.Properties;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigLoaderTest {
    @Mock
    private Log log;

    @Test
    public void testLoadProperties() {
        ConfigLoader loader = new ConfigLoader("src/test/resources", log);
        Properties props = loader.loadProperties("");
        Assert.assertEquals(2, props.size());
        Assert.assertTrue(props.containsKey("db.url"));
    }
}
