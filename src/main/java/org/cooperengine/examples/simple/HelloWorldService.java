/*
 * Copyright 2002-2014 SCOOP Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cooperengine.examples.simple;

import org.copperengine.core.Acknowledge;
import org.copperengine.core.ProcessingEngine;
import org.copperengine.core.Response;

public class HelloWorldService {

    private static HelloWorldService service;
    private ProcessingEngine engine;

    public HelloWorldService(ProcessingEngine engine) {
        this.engine = engine;
    }

    public static void init(ProcessingEngine engine) {
        service = new HelloWorldService(engine);
    }

    public static HelloWorldService get() {
        return service;
    }

    /**
     * called by the external adapter to send the answer back.
     * @param correlationId
     * @param success
     * @param answer
     */
    public void sendResponse(String correlationId, boolean success, String answer) {
        HelloWorldResponse payload = new HelloWorldResponse(success, answer);
        Response<HelloWorldResponse> response = new Response<HelloWorldResponse>(correlationId, payload, null);
        Acknowledge.DefaultAcknowledge ack = new Acknowledge.DefaultAcknowledge();
        engine.notify(response, ack);
        ack.waitForAcknowledge();
    }


}