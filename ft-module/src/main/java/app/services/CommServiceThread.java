/*
 * ******************************************************************************
 * Copyright 2017 Contributors to Exact Sciences Institute, Department Computer Science, University of Brasília - UnB
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package app.services;

import static app.models.AttemptsNumber.DEFAULT_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import app.commons.http.Response;
import app.commons.utils.LoggerUtil;
import app.conf.Routes;
import app.models.Level;

public class CommServiceThread implements Runnable {

    private static final String BASE_URL = "http://localhost:%s/%s";
    private static final String ARG_MODULE_ID = "{moduleId}";
    private static final String MODULE_ID = ManagementFactory.getRuntimeMXBean().getName();
    @Value("${server.port}")
    private static final int PORT = 7777;
    private CommStatus status = CommStatus.STOPPED;
    private Level level;

    @Autowired
    private TestRestTemplate restTemplate;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CommServiceThread(final Level ftLevel) {
        super();
        this.level = ftLevel;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see java.lang.Runnable#run()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void run() {

        int attemptsNumber = 0;
        while (attemptsNumber++ < DEFAULT_VALUE && !this.register()) {
            this.status = CommStatus.STARTED;
        }
        if (CommStatus.STARTED != this.status) {
            this.status = CommStatus.ERROR;
            return;
        }
        while (true) {
            try {

                System.out.println("atenção... pingando o FTCoord para o FTModule: " + MODULE_ID);

                TimeUnit.SECONDS.sleep(DEFAULT_VALUE);
            } catch (final InterruptedException e) {
                LoggerUtil.error(e);
            }
        }
    }

    public boolean register() {
        final String path = Routes.REGISTER.replace(ARG_MODULE_ID, MODULE_ID);
        final String url = String.format(BASE_URL, PORT, path);
        final ResponseEntity<Response> response = this.restTemplate.postForEntity(url, this.level, Response.class);
        return response.getBody().getStatus() == OK;
    }

    public void imalive() throws Exception {
        final String path = Routes.IMALIVE;
        final String url = String.format(BASE_URL, PORT, path);
        final Response result = this.restTemplate.getForObject(url, Response.class);
        assertThat(result.getStatus()).isEqualTo(CREATED);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Level getFtLevel() {
        return this.level;
    }

    public void setFtLevel(final Level ftLevel) {
        this.level = ftLevel;
    }

    public CommStatus getStatus() {
        return this.status;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner Enums.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public enum CommStatus {
        ERROR,
        STOPPED,
        STARTED
    }
}
