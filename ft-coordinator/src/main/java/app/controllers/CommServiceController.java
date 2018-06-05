/*
 *******************************************************************************
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
 ******************************************************************************
 */
package app.controllers;

import static app.commons.enums.SystemEnums.ExecutionStatus.RECOVERY_MODE;
import static app.commons.enums.SystemEnums.ExecutionStatus.STARTED;
import static app.conf.Routes.IMALIVE;
import static app.conf.Routes.REGISTER;
import static app.conf.Routes.SHUTDOWN;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.lang.management.ManagementFactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.commons.enums.SystemEnums.ExecutionStatus;
import app.commons.http.Response;
import app.commons.utils.LoggerUtil;
import app.models.Level;
import app.services.CommService;
import app.services.FaultToleranceService;

@RestController
public class CommServiceController implements CommService {

    private static final String COULD_NOT_INITIALIZE_FAULT_TOLERANCE_SERVICES = "Could not initialize fault tolerance services. Check the log files.";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.services.CommService#imalive(java.lang.String)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    @RequestMapping(value = IMALIVE, method = GET)
    public @ResponseBody Response imalive(@PathVariable(value = "moduleId") final String moduleId) {

        LoggerUtil.info(" imalive called for: " + moduleId);
        LoggerUtil.info(" my PID is: " + ManagementFactory.getRuntimeMXBean().getName());
        return new Response(OK, moduleId);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.services.CommService#register(app.models.Level)
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    @RequestMapping(value = REGISTER, method = POST)
    public @ResponseBody Response register(@RequestBody final Level level) {

        final ExecutionStatus serviceStatus = FaultToleranceService.getStatus();
        if (STARTED != serviceStatus && RECOVERY_MODE != serviceStatus) {
            FaultToleranceService.startDetectionServices(level);
        }
        return FaultToleranceService.getStatus() != STARTED
                ? new Response(INTERNAL_SERVER_ERROR, COULD_NOT_INITIALIZE_FAULT_TOLERANCE_SERVICES)
                : new Response(CREATED);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see app.services.CommService#shutdown()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    @RequestMapping(value = SHUTDOWN, method = GET)
    public @ResponseBody Response shutdown(@PathVariable(value = "moduleId") final String moduleId) {

        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(SHUTDOWN_TIME);
                System.exit(0);
            } catch (final InterruptedException e) {
                LoggerUtil.error(e);
            }
        });
        return new Response(OK);
    }
}