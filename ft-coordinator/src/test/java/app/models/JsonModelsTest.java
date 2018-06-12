package app.models;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonModelsTest {

    protected static final int ZOO_PORT = 2181;
    protected static final String ZOO_IP = "0.0.0.0";
    protected static final String RUNTIME_MODULE_ID = ManagementFactory.getRuntimeMXBean().getName();
    protected final ArrayList<CloudInstance> lstFakeNodes = new ArrayList<>();
    protected final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT, new Credentials("userZoo", "passZoo01"));
    private final Level ftLevel;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public JsonModelsTest() {
        super();
        this.ftLevel = new Level("only json serialize test", 5L);
        this.lstFakeNodes.add(new CloudInstance("10.0.0.1", 21, new Credentials("user01", "pass01")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.2", 22, new Credentials("user02", "pass02")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.3", 23, new Credentials("user03", "pass03")));
        this.ftLevel.addTechnique(new SoftwareRejuvenation(97, 95));
        this.ftLevel.addTechnique(new Retry());
        this.ftLevel.addTechnique(new TaskResubmission());
        this.ftLevel.addTechnique(new Replication(this.lstFakeNodes));
    }

    @Test
    public void serializeTest() {

        String json = null;
        try {
            json = this.ftLevel.toJson();
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(json);
    }

    @Test
    public void deserializeTest() {

        Level object = null;
        try {
            object = new ObjectMapper().readValue(this.ftLevel.toJson(), Level.class);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(object);
    }
}
