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

import static app.commons.constants.TimeConstants.DEFAULT_TIME_UNIT;
import static app.commons.enums.SystemEnums.ExecutionStatus.ERROR;
import static app.commons.enums.SystemEnums.ExecutionStatus.STARTED;
import static app.commons.enums.SystemEnums.ExecutionStatus.STOPPED;
import static app.conf.Routes.IMALIVE;
import static app.conf.Routes.MODULE_ID;
import static app.conf.Routes.REGISTER;
import static app.conf.Routes.SHUTDOWN;
import static app.models.AttemptsNumber.DEFAULT_VALUE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.lang.management.ManagementFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import app.commons.enums.SystemEnums.ExecutionStatus;
import app.commons.http.Response;
import app.commons.utils.LoggerUtil;
import app.models.Level;

public class CommServiceThread implements Runnable {

    private static final int PORT = 7777;
    private static final String BASE_URL = "http://localhost:%s%s";
    private static final String ARG_MODULE_ID = ManagementFactory.getRuntimeMXBean().getName();
    private final RestTemplate restTemplate = new RestTemplate();
    private Level level;
    private ExecutionStatus status = STOPPED;
    private boolean registered;

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
        this.establishCommunication();
        if (STARTED != this.status) {
            this.status = ERROR;
            return;
        }
        this.registerFtModule();
        if (!this.isRegistered()) {
            this.status = ERROR;
            return;
        }
        while (STARTED == this.status) {
            try {
                this.callRequest(IMALIVE);
                DEFAULT_TIME_UNIT.sleep(DEFAULT_VALUE);
            } catch (final InterruptedException e) {
                LoggerUtil.error(e);
            }
        }
    }

    public boolean callRequest(final String route) {
        return this.callRequest(route, false);
    }

    public boolean callRequest(final String route, final boolean skipErrorLog) {
        try {
            switch (route) {
                case REGISTER:
                    return this.register();
                case IMALIVE:
                    return this.imalive();
                case SHUTDOWN:
                    return this.shutdown();
                default:
                    break;
            }
        } catch (final RestClientException e) {
            if (!skipErrorLog) {
                LoggerUtil.error(e);
            }
        }
        return false;
    }

    private void registerFtModule() {
        int attemptsNumber = 0;
        while (attemptsNumber++ < DEFAULT_VALUE) {
            if (this.callRequest(REGISTER)) {
                this.registered = true;
                break;
            }
        }
    }

    private void establishCommunication() {
        int attemptsNumber = 0;
        while (attemptsNumber++ < DEFAULT_VALUE) {
            if (this.callRequest(IMALIVE)) {
                this.status = STARTED;
                break;
            }
        }
    }

    private boolean register() {
        final String url = String.format(BASE_URL, PORT, REGISTER);
        final ResponseEntity<Response> response = this.restTemplate.postForEntity(url, this.level, Response.class);
        return response.getBody().getStatus() == CREATED;
    }

    private boolean imalive() {
        final String path = IMALIVE.replace(MODULE_ID, ARG_MODULE_ID);
        final String url = String.format(BASE_URL, PORT, path);
        final Response result = this.restTemplate.getForObject(url, Response.class);
        return result.getStatus() == OK;
    }

    private boolean shutdown() {
        final String path = SHUTDOWN.replace(MODULE_ID, ARG_MODULE_ID);
        final String url = String.format(BASE_URL, PORT, path);
        final Response result = this.restTemplate.getForObject(url, Response.class);
        if (result.getStatus() == OK) {
            this.status = STOPPED;
        }
        return this.status == STOPPED;
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

    public ExecutionStatus getStatus() {
        return this.status;
    }

    public boolean isRegistered() {
        return this.registered;
    }

}
