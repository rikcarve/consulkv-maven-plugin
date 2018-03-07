package ch.carve.maven.consulkv;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class ConfigLoaderTest {

    @Test
    public void testLoadProperties() {
        ConfigLoader loader = new ConfigLoader("src/test/resources", this::warn);
        Properties props = loader.loadProperties("");
        Assert.assertEquals(3, props.size());
        Assert.assertTrue(props.containsKey("db.url"));
        Assert.assertTrue(props.containsKey("test2"));
    }

    private void warn(CharSequence bla) {

    }
}
