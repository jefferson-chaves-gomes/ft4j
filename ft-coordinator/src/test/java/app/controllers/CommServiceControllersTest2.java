package app.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.management.ManagementFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import app.conf.Routes;
import app.models.Level;
import app.models.ZooInstance;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommServiceControllersTest2 {

    private final static String BASE_URL = "http://localhost:%s/%s";

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void imalive() throws Exception {

        final String moduleId = ManagementFactory.getRuntimeMXBean().getName();
        final String path = Routes.IMALIVE.replace("{moduleId}", moduleId);
        final String expected = String.format("%s still alive", moduleId);
        final String url = String.format(BASE_URL, this.port, path);

        final String result = this.restTemplate.getForObject(url, String.class);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void register() {

        final String path = Routes.REGISTER;
        final String url = String.format(BASE_URL, this.port, path);

        final Level level = new Level(new ZooInstance("0.0.0.0", 7777));
        final ResponseEntity<String> postResponse = this.restTemplate.postForEntity(url, level, String.class);
        System.out.println("Response for Post Request: " + postResponse.getBody());
    }
}