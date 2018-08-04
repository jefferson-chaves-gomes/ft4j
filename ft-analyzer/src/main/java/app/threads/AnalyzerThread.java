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
package app.threads;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import app.commons.utils.LoggerUtil;

@Component
public class AnalyzerThread extends Thread {

    private static final int REQUEST_TIME = 1000;
    private static final String BREAK_LINE = "";
    private static final String LOG_SEPARATOR = "***************************************";
    private static final String HEADER = "Tempo Avaliado     Tempo Disponível    Tempo Indisponível      Total de Requisições    Respostas Bem Sucedidas     MTTR              Disponibilidade     Confiabilidade";
    private static final String RESULTS = "%d                 %d                  %d                      %d                      %d                          %f                %f                  %f";
    private static final String CSV_HEADER = "\n\nTempo Avaliado,Tempo Disponível,Tempo Indisponível,Total de Requisições,Respostas Bem Sucedidas,MTTR,Disponibilidade,Confiabilidade";
    private static final String CSV_RESULTS = "\n%d,%d,%d,%d,%d,%f,%f,%f\n\n";
    private static final int FAILURE = 1;
    private static final int SUCCESS = 0;
    private static final int TEST_TIME = 1;
    private static final RestTemplate restTemplate = new RestTemplate();

    private String baseUrl;
    private long totalAvailableTime = 0;
    private long totalUnavailableTime = 0;
    private long totalRequests = 0;
    private long totalSuccessfulRequests = 0;
    private long failureCount = 0;
    private long responseStatus = SUCCESS;
    private boolean isAfterTest1h = false;
    private boolean isAfterTest2h = false;
    private boolean isAfterTest4h = false;
    private boolean isAfterTest8h = false;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public AnalyzerThread() {
        super();
    }

    public AnalyzerThread(final String baseUrl) {
        super();
        this.baseUrl = baseUrl;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // * @see java.lang.Thread#run()
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void run() {

        LoggerUtil.info(BREAK_LINE);
        LoggerUtil.info(LOG_SEPARATOR);
        LoggerUtil.info("Starting ft-analyzer...");
        LoggerUtil.info("Cheking URL: " + this.baseUrl);
        LoggerUtil.info(LOG_SEPARATOR);

        final Instant test1h = Instant.now().plus(TEST_TIME, ChronoUnit.HOURS);
        final Instant test2h = Instant.now().plus(TEST_TIME * 2, ChronoUnit.HOURS);
        final Instant test4h = Instant.now().plus(TEST_TIME * 3, ChronoUnit.HOURS);
        final Instant test8h = Instant.now().plus(TEST_TIME * 4, ChronoUnit.HOURS);

        this.startFaultInjection();

        String areYouAlive = "false";
        do {
            this.totalRequests++;

            final Instant start = Instant.now();
            try {
                areYouAlive = restTemplate.getForObject(this.baseUrl, String.class);
            } catch (final Exception e) {
                areYouAlive = "false";
            }
            final Instant end = Instant.now();

            final long requestTime = Duration.between(start, end).toMillis() + REQUEST_TIME;

            try {
                TimeUnit.MILLISECONDS.sleep(REQUEST_TIME);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

            if (Boolean.valueOf(areYouAlive)) {
                if (this.responseStatus == FAILURE) {
                    this.startFaultInjection();
                }
                this.responseStatus = SUCCESS;
                this.totalAvailableTime += requestTime;
                this.totalSuccessfulRequests++;
            } else {
                if (this.responseStatus == SUCCESS) {
                    this.failureCount++;
                    LoggerUtil.info("Failure Detected - Number: " + this.failureCount);
                }
                this.responseStatus = FAILURE;
                this.totalUnavailableTime += requestTime;
            }

            final Instant now = Instant.now();
            if (!this.isAfterTest1h && test1h.compareTo(now) <= 0) {
                this.isAfterTest1h = true;
                this.logAnalysis("01 hour");
            } else if (!this.isAfterTest2h && test2h.compareTo(now) <= 0) {
                this.isAfterTest2h = true;
                this.logAnalysis("02 hour");
            } else if (!this.isAfterTest4h && test4h.compareTo(now) <= 0) {
                this.isAfterTest4h = true;
                this.logAnalysis("03 hour");
            } else if (!this.isAfterTest8h && test8h.compareTo(now) <= 0) {
                this.isAfterTest8h = true;
                this.logAnalysis("04 hour");
            }

        } while (Instant.now().compareTo(test8h) <= 0);

        System.exit(0);
    }

    private void startFaultInjection() {
        LoggerUtil.info("Starting FaultInjection....");
        final String startFaultInjection = restTemplate.getForObject(this.baseUrl.replace("imalive", "startFaultInjection"), String.class);
        LoggerUtil.info(startFaultInjection);
    }

    private void logAnalysis(final String time) {

        final Double mttr = this.failureCount == 0 ? 0
                : this.totalUnavailableTime / (double) this.failureCount;
        final Double availability = this.totalAvailableTime == 0 ? 0
                : (this.totalAvailableTime - this.totalUnavailableTime) / (double) this.totalAvailableTime * 100.00;
        final Double reliability = this.totalSuccessfulRequests == 0 ? 0
                : this.totalSuccessfulRequests / (double) this.totalRequests * 100.00;

        LoggerUtil.info(BREAK_LINE);
        LoggerUtil.info(LOG_SEPARATOR);
        LoggerUtil.info("*******          " + time + "        ******* :" + this.failureCount);
        LoggerUtil.info(BREAK_LINE);
        LoggerUtil.info(BREAK_LINE);
        LoggerUtil.info(HEADER);
        LoggerUtil.info(String.format(RESULTS,
                this.totalAvailableTime + this.totalUnavailableTime,
                this.totalAvailableTime,
                this.totalUnavailableTime,
                this.totalRequests,
                this.totalSuccessfulRequests,
                mttr,
                availability,
                reliability));

        LoggerUtil.info(BREAK_LINE);
        LoggerUtil.info(BREAK_LINE);

        LoggerUtil.info(CSV_HEADER + String.format(CSV_RESULTS,
                this.totalAvailableTime + this.totalUnavailableTime,
                this.totalAvailableTime,
                this.totalUnavailableTime,
                this.totalRequests,
                this.totalSuccessfulRequests,
                mttr,
                availability,
                reliability));

        LoggerUtil.info(BREAK_LINE);
        LoggerUtil.info(BREAK_LINE);
        LoggerUtil.info(LOG_SEPARATOR);
        LoggerUtil.info(BREAK_LINE);
    }
}
