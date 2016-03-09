package hmi.environmentbase;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * unit tests for the configdirloader
 * @author hvanwelbergen
 *
 */
public class ConfigDirLoaderTest
{
    private ConfigDirLoader loader;
    
    @Before
    public void setup()
    {
        System.setProperty("user.dir","/test");
        System.setProperty("shared.project.root","/testroot");        
        loader = new ConfigDirLoader("CONF","CONFTAG");        
    }
    
    @Test
    public void testDefault()
    {
        assertEquals("/test/lib/CONF",loader.getConfigDir());
    }
    
    @Test
    public void testDefaultWithParse()
    {
        String config = "<CONFTAG/>";
        loader.readXML(config);
        assertEquals("/test/lib/CONF",loader.getConfigDir());
    }
    
    @Test
    public void testGlobal()
    {
        String config = "<CONFTAG dir=\"globaldir\"/>";
        loader.readXML(config);
        assertEquals("globaldir",loader.getConfigDir());
    }
    
    @Test
    public void testLocal()
    {
        String config = "<CONFTAG localdir=\"localdir\"/>";
        loader.readXML(config);
        assertEquals("/testroot/localdir",loader.getConfigDir());
    }
}
