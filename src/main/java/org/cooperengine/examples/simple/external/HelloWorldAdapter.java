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
package org.cooperengine.examples.simple.external;

import org.cooperengine.examples.simple.HelloWorldRequest;
import org.cooperengine.examples.simple.HelloWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The external Hello World Adapter.
 * In a more realistic example this would be an external async web service that gives back an TicketID (correlationId)
 * and sends back an answer after some processing time.
 */
public class HelloWorldAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldAdapter.class);

    private static LinkedHashMap<String, HelloWorldRequest> data = new LinkedHashMap<String, HelloWorldRequest>();
    private static HelloWorldAdapter adapter;
    int count = 0;

    /**
     * Inside the Workflow we need to access the adapter.
     * in orchestration example we will replace this with Injection by spring, guice or self written.
     * @return
     */
    public static HelloWorldAdapter get() {
        if (adapter == null) {
            adapter = new HelloWorldAdapter();
        }
        return adapter;
    }

    public HelloWorldAdapter() {
    }

    /**
     * receive the Hello World message and answer async with the correlationId
     * @param request
     * @return correlationId
     */
    public String asyncSendHelloWorld(final HelloWorldRequest request) {

        final String correlationId = "ID" + count++;
        data.put(correlationId, request);
        runThread(correlationId);
        return correlationId;
    }


    /**
     * send the answer back to the the Hello World application
     * @param correlationId
     */
    private void runThread(final String correlationId) {
        Thread someThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HelloWorldRequest request = data.get(correlationId);
                if (request == null) {
                    logger.warn("no entry found for ID:" + correlationId);
                    return;
                }

                String answer = "Hello " + request.getSenderName() + "!";
                HelloWorldService.get().sendResponse(correlationId, true, answer);
            }
        });
        someThread.setDaemon(true);
        someThread.start();
    }
}
