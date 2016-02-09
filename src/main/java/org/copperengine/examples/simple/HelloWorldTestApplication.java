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
package org.copperengine.examples.simple;

import java.io.File;

import org.copperengine.core.CopperException;
import org.copperengine.core.tranzient.TransientScottyEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HelloWorldTestApplication {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldTestApplication.class);
    private static int NUMBER_OF_WORKFLOWS = 5000;

    private TransientScottyEngine engine;

    private HelloWorldTestApplication() {
    }

    public static void main(String args[]) throws Exception {
        new HelloWorldTestApplication().run();
    }

    private void run() throws InterruptedException {

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
                // Question: why not return the wfinstance id in the run(...) method?
                // String wfId = engine.run("HelloWorldWorkFlow", getNewData());
                // logger.info("Process HelloWorldWorkFlow running with id="+wfId);
                engine.run("HelloWorldWorkFlow", getNewData());
            } catch (CopperException e) {
                logger.error("copper error: ", e);
            }
        }

        // wait for all workflow instances to finish
        for (; engine.getNumberOfWorkflowInstances() > 0;) {
            logger.info(engine.getNumberOfWorkflowInstances() + " wf remaining..");
            Thread.sleep(100);
        }
        engine.shutdown();
    }

    public HelloWorldRequest getNewData() {
        return new HelloWorldRequest("Developer");
    }
}
