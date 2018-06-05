package app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import app.commons.http.Response;
import app.conf.Routes;
import app.models.CloudInstance;
import app.models.Credentials;
import app.models.Level;
import app.models.Replication;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.ZooInstance;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ControllersRestTemplateTest {

    private final static String BASE_URL = "http://localhost:%s%s";
    private final static String STARTUP_COMMAND = "java -jar ../ft-coordinator/target/ft-coordinator-0.0.1.jar";
    private final static String ZOO_IP = "0.0.0.0";
    private final static int ZOO_PORT = 2181;
    private final ZooInstance zooInstance = new ZooInstance(ZOO_IP, ZOO_PORT, new Credentials("userZoo", "passZoo01"));
    private final Level level = new Level(STARTUP_COMMAND, this.zooInstance);
    private final ArrayList<CloudInstance> lstFakeNodes = new ArrayList<>();
    private final static String MODULE_ID = ManagementFactory.getRuntimeMXBean().getName();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public ControllersRestTemplateTest() {
        super();
        this.lstFakeNodes.add(new CloudInstance("10.0.0.1", 21, new Credentials("user01", "pass01")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.2", 22, new Credentials("user02", "pass02")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.3", 23, new Credentials("user03", "pass03")));
        this.level.setModuleId(MODULE_ID);
        this.level.addTechnique(new SoftwareRejuvenation());
        this.level.addTechnique(new Retry());
        this.level.addTechnique(new TaskResubmission());
        this.level.addTechnique(new Replication(this.lstFakeNodes));
    }

    @Value("${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void imalive() throws Exception {

        final String path = Routes.IMALIVE.replace("{moduleId}", MODULE_ID);
        final String url = String.format(BASE_URL, this.port, path);

        final Response result = this.restTemplate.getForObject(url, Response.class);
        assertThat(result.getStatus()).isEqualTo(OK);
    }

    @Test
    public void register1() {

        final String path = Routes.REGISTER;
        final String url = String.format(BASE_URL, this.port, path);

        final ResponseEntity<Response> result = this.restTemplate.postForEntity(url, this.level, Response.class);
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody().getStatus()).isEqualTo(CREATED);
    }

    @Test
    public void register2() {

        final String path = Routes.REGISTER;
        final String url = String.format(BASE_URL, this.port, path);

        final ResponseEntity<Response> result = this.restTemplate.postForEntity(url, this.level, Response.class);
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody().getStatus()).isEqualTo(CREATED);
    }
}