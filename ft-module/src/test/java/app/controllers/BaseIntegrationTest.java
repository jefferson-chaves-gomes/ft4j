package app.controllers;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import app.FaultToleranceModule;
import app.models.CloudInstance;
import app.models.Credentials;
import app.models.ZooInstance;
import app.services.BootstrapService;

public class BaseIntegrationTest {

    protected final static int ZOO_PORT = 2181;
    protected final static String ZOO_IP = "0.0.0.0";
    protected final static String MODULE_ID = ManagementFactory.getRuntimeMXBean().getName();
    protected final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT, new Credentials("userZoo", "passZoo01"));
    protected final ArrayList<CloudInstance> lstFakeNodes = new ArrayList<>();

    protected final FaultToleranceModule ftModule = BootstrapService.getInstance();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public BaseIntegrationTest() {
        super();
        this.lstFakeNodes.add(new CloudInstance("10.0.0.1", 21, new Credentials("user01", "pass01")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.2", 22, new Credentials("user02", "pass02")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.3", 23, new Credentials("user03", "pass03")));
    }
}
