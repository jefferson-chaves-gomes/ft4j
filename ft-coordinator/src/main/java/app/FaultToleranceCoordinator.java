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
package app;

import java.time.LocalDateTime;

import app.models.Level;

public class FaultToleranceCoordinator {

    public static LocalDateTime aliveTime;
    private Level level;

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(final Level level) {
        this.level = level;
    }

    public void register(final Level ftLevel) {
        this.level = ftLevel;
    }
}
