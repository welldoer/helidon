/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.microprofile.metrics;

import java.util.stream.IntStream;

import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class HelloWorldTest.
 *
 * @author Santiago Pericas-Geertsen
 */
public class HelloWorldTest extends MetricsMpServiceTest {

    @BeforeEach
    public void registerCounter() {
        registerCounter("helloCounter");
    }

    @Test
    public void testMetrics() {
        IntStream.range(0, 5).forEach(
                i -> client.target(BASE_URL)
                        .path("helloworld").request().accept(MediaType.TEXT_PLAIN_TYPE)
                        .get(String.class));
        assertEquals(5, getCounter("helloCounter").getCount());
    }

    @AfterEach
    public void checkMetricsUrl() {
        JsonObject app = client.target(BASE_URL)
                .path("metrics").request().accept(MediaType.APPLICATION_JSON_TYPE)
                .get(JsonObject.class).getJsonObject("application");
        assertEquals(5, app.getJsonNumber("helloCounter").intValue());
    }
}
