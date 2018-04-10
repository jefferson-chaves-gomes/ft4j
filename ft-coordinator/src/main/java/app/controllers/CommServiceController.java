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

import static app.conf.Routes.IMALIVE;
import static app.conf.Routes.REGISTER;
import static app.conf.Routes.SHUTDOWN;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import app.commons.http.Response;
import app.models.Level;
import app.services.CommService;

@RestController
public class CommServiceController implements CommService {

    private static long lastCommunication;

    @Override
    @RequestMapping(value = IMALIVE, method = GET)
    public @ResponseBody Response imalive(@PathVariable(value = "moduleId") final String moduleid) {

        final long currentTime = System.currentTimeMillis();
        if (currentTime - lastCommunication > 50 * 1000) {

        }
        lastCommunication = currentTime;
        return new Response(OK);
    }

    @Override
    @RequestMapping(value = REGISTER, method = POST)
    public @ResponseBody Response register(@RequestBody final Level level) {
        return new Response(CREATED);
    }

    @Override
    @RequestMapping(value = SHUTDOWN, method = GET)
    public @ResponseBody Response shutdown() {
        return new Response(OK);
    }
}