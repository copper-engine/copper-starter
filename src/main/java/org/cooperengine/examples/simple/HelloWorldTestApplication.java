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

import org.copperengine.core.CopperException;
import org.copperengine.core.tranzient.TransientScottyEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class HelloWorldTestApplication {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldTestApplication.class);
    private static int NUMBER_OF_WORKFLOWS = 5000;

    private TransientScottyEngine engine;

    private HelloWorldTestApplication() {
    }

    public static void main(String args[]) throws Exception {
        new HelloWorldTestApplication().run();
    }

    private void run() {

        // initialize the procession engine the default configuration and source directory for the workflow files
        TransientEngineFactory factory = new TransientEngineFactory() {
            @Override
            protected File getWorkflowSourceDirectory() {
                return new File("./src/workflow/java");
            }
        };

        //Startup the engine
        engine = factory.create();

        // HelloWorldService provides an API to be called by the external adapter and needs to notify the engine.
        HelloWorldService.init(engine);

        //start 5000 workflows with request data
        for (int i = 0; i < NUMBER_OF_WORKFLOWS; i++) {
            try {
                engine.run("HelloWorldWorkFlow", getNewData());
            } catch (CopperException e) {
                logger.error("copper error: ", e);
            }
        }

        engine.shutdown();
    }

    public HelloWorldRequest getNewData() {
        return new HelloWorldRequest("Developer");
    }
}
