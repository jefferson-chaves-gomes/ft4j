package app.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.lang.management.ManagementFactory;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import app.commons.http.Response;
import app.conf.Routes;
import app.models.Level;
import app.models.ZooInstance;

// @RunWith(SpringRunner.class)
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllersTest2 {

    private final static String BASE_URL = "http://localhost:%s/%s";
    private final static String STARTUP_COMMAND = "java -jar ../ft-coordinator/target/ft-coordinator-0.0.1.jar";

    //    @LocalServerPort
    private int port;
    //    @Autowired
    private TestRestTemplate restTemplate;

    //    @Test
    public void imalive() throws Exception {

        final String moduleId = ManagementFactory.getRuntimeMXBean().getName();
        final String path = Routes.IMALIVE.replace("{moduleId}", moduleId);
        final String url = String.format(BASE_URL, this.port, path);

        final Response result = this.restTemplate.getForObject(url, Response.class);
        assertThat(result.getStatus()).isEqualTo(OK);
    }

    //    @Test
    public void register() {

        final String path = Routes.REGISTER;
        final String url = String.format(BASE_URL, this.port, path);

        final Level level = new Level(STARTUP_COMMAND, new ZooInstance("0.0.0.0", 7777));
        final ResponseEntity<Response> result = this.restTemplate.postForEntity(url, level, Response.class);
        assertThat(result.getStatusCode()).isEqualTo(OK);
        assertThat(result.getBody().getStatus()).isEqualTo(CREATED);
    }
}