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
package app.models;

public class Technic {

    private int attemptsNumber = 3;
    private int delayBetweenAttempts = 1;
    private int timeout = 10;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Technic() {
        super();
    }

    public Technic(final int attemptsNumber, final int delayBetweenAttempts, final int timeout) {
        super();
        this.attemptsNumber = attemptsNumber;
        this.delayBetweenAttempts = delayBetweenAttempts;
        this.timeout = timeout;
    }

    public int getAttemptsNumber() {
        return this.attemptsNumber;
    }

    public void setAttemptsNumber(final int attemptsNumber) {
        this.attemptsNumber = attemptsNumber;
    }

    public int getDelayBetweenAttempts() {
        return this.delayBetweenAttempts;
    }

    public void setDelayBetweenAttempts(final int delayBetweenAttempts) {
        this.delayBetweenAttempts = delayBetweenAttempts;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
}
