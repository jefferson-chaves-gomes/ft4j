package app.controllers;

import static app.commons.constants.TimeConstants.DEFAULT_HEARTBEAT_TIME;
import static app.conf.Routes.IMALIVE;
import static app.conf.Routes.LATENCY_MILLES;
import static app.conf.Routes.MODULE_ID;
import static app.conf.Routes.REGISTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
import app.models.CloudInstance;
import app.models.Credentials;
import app.models.Level;
import app.models.Retry;
import app.models.SoftwareRejuvenation;
import app.models.TaskResubmission;
import app.models.Timeout;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ControllersRestTemplateTest {

    private final static String BASE_URL = "http://localhost:%s%s";
    private final static String STARTUP_COMMAND = "touch zzz-createdBy-ControllersRestTemplateTest_.txt" + Instant.now();
    private final static String RUNTIME_MODULE_ID = ManagementFactory.getRuntimeMXBean().getName();
    private static Long latencyMilles = 0L;
    private final ArrayList<CloudInstance> lstFakeNodes = new ArrayList<>();
    private final Level level;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public ControllersRestTemplateTest() {
        super();
        this.lstFakeNodes.add(new CloudInstance("10.0.0.1", 21, new Credentials("user01", "pass01")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.2", 22, new Credentials("user02", "pass02")));
        this.lstFakeNodes.add(new CloudInstance("10.0.0.3", 23, new Credentials("user03", "pass03")));
        this.level = new Level(STARTUP_COMMAND, DEFAULT_HEARTBEAT_TIME);
        this.level.setModuleId(RUNTIME_MODULE_ID);
    }

    @Value("${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void imalive() throws Exception {

        final String path = IMALIVE.replace(MODULE_ID, RUNTIME_MODULE_ID).replace(LATENCY_MILLES, latencyMilles.toString());
        final String url = String.format(BASE_URL, this.port, path);

        final Instant start = Instant.now();
        final Response result1 = this.restTemplate.getForObject(url, Response.class);
        assertThat(result1.getStatus()).isEqualTo(OK);
        latencyMilles = Duration.between(start, Instant.now()).toMillis();
    }

    @Test
    public void retryPassingLatency() throws Exception {

        this.level.addTechnique(new Retry(new Timeout(1L, TimeUnit.SECONDS)));
        this.register();
        long fakeLatency = 1;
        for (int i = 0; i < 5; i++) {
            final Instant start = Instant.now();
            this.imalive();
            TimeUnit.SECONDS.sleep(fakeLatency++);
            latencyMilles = Duration.between(start, Instant.now()).toMillis();
        }
        assertThat(true);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // these tests below must be performed in isolation, one at a time (comment the annotation @Test)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //    @Test
    public void softwareRejuvenation() throws Exception {

        this.level.addTechnique(new SoftwareRejuvenation(new Timeout(30L, TimeUnit.SECONDS), 97, 90));
        this.register();
        TimeUnit.SECONDS.sleep(35);
        System.out.println("this line should not be displayed... the SoftwareRejuvenation technique must kill the process before that... ensure this by log checking");
    }

    //    @Test
    public void retry() throws Exception {

        this.level.addTechnique(new Retry(new Timeout(1L, TimeUnit.SECONDS)));
        this.register();
        long fakeLatency = 1;
        for (int i = 0; i < 15; i++) {
            this.imalive();
            TimeUnit.SECONDS.sleep(fakeLatency++);
        }
        System.out.println("this line should not be displayed... the Retry technique must kill the process before that... ensure this by log checking");
    }

    //    @Test
    public void taskResubmission() throws Exception {

        this.level.addTechnique(new TaskResubmission(new Timeout(1L, TimeUnit.SECONDS)));
        this.register();
        long fakeLatency = 1;
        for (int i = 0; i < 15; i++) {
            this.imalive();
            TimeUnit.SECONDS.sleep(fakeLatency++);
        }
        System.out.println("this line should not be displayed... the TaskResubmission technique must kill the process before that... ensure this by log checking");
    }

    private void register() {

        final String path = REGISTER;
        final String url = String.format(BASE_URL, this.port, path);

        final ResponseEntity<Response> result = this.restTemplate.postForEntity(url, this.level, Response.class);
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody().getStatus()).isEqualTo(CREATED);
    }
}