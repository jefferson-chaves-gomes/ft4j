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
package app.commons.utils;

import java.util.List;
import java.util.stream.Collectors;

import app.commons.enums.SystemEnums.FaultToletancePolicy;
import app.models.Technique;

public final class StreamUtil {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors.
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public StreamUtil() {
        super();
    }

    public static <T> boolean hasDuplicates(final List<T> list, final Class<?> cls) {
        if (list == null) {
            return false;
        }
        return list.stream()
                .filter(cls::isInstance)
                .count() > 1;
    }

    public static <T> boolean hasFaultToleranceTechinique(final List<T> list, final Class<?> cls) {
        if (list == null) {
            return false;
        }
        return list.stream()
                .filter(cls::isInstance)
                .count() > 0;
    }

    public static boolean hasFaultTolerancePolicy(final List<Technique> list, final FaultToletancePolicy ftType) {
        if (list == null) {
            return false;
        }
        return list.stream()
                .filter(x -> x.getFtType() == ftType)
                .count() > 0;
    }

    public static String toString(final List<String> lstCommands) {
        return lstCommands.stream().collect(Collectors.joining());
    }
}