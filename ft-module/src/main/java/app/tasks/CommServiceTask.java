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
package app.tasks;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import app.commons.utils.LoggerUtil;
import app.commons.utils.RuntimeUtil;
import app.commons.utils.RuntimeUtil.Command;
import app.models.Level;

public class CommServiceTask implements Runnable {

    private static final String FT_COORDINATOR_STARTUP_COMMAND = "java -jar z-spring-boott-test-0.0.1-SNAPSHOT.jar";
    private final Command command = new Command(FT_COORDINATOR_STARTUP_COMMAND);
    private Level ftLevel;
    private Process commServiceProcess;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public CommServiceTask(final Level ftLevel) {
        super();
        this.ftLevel = ftLevel;
        this.startFtCoordinator();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see java.lang.Runnable#run()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void run() {
        while (true) {
            try {
                if (this.commServiceProcess != null && this.commServiceProcess.isAlive()) {
                    TimeUnit.SECONDS.sleep(60);
                } else {
                    this.startFtCoordinator();
                }
            } catch (final InterruptedException e) {
                LoggerUtil.error(e);
            }
        }
    }

    private void startFtCoordinator() {
        try {
            this.commServiceProcess = RuntimeUtil.exec(this.command);
        } catch (IOException | InterruptedException e) {
            LoggerUtil.error(e);
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // get/set.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Level getFtLevel() {
        return this.ftLevel;
    }

    public void setFtLevel(final Level ftLevel) {
        this.ftLevel = ftLevel;
    }

}
