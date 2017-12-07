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

import org.copperengine.core.Interrupt;
import org.copperengine.core.Response;
import org.copperengine.core.WaitMode;
import org.copperengine.core.Workflow;
import org.copperengine.core.WorkflowDescription;
import org.copperengine.examples.simple.external.HelloWorldAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

@WorkflowDescription(alias = "HelloWorldWorkFlow", majorVersion = 1, minorVersion = 0, patchLevelVersion = 0)
public class HelloWorldWorkFlow extends Workflow<HelloWorldRequest> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldWorkFlow.class);

    @Override
    public void main() throws Interrupt {
        final String correlationId = HelloWorldAdapter.get().asyncSendHelloWorld(getData());
        logger.info(correlationId + ": workflow    break: Hello World!");
        //        logger.info(correlationId+ ": workflow   part one: Hello World!");

//        IntStream.range(1, 10_000_000).forEach(System.out::println);

        wait(WaitMode.ALL, 5 * 60 * 60 * 1000, correlationId);
        final Response<HelloWorldResponse> response = getAndRemoveResponse(correlationId);
        logger.info(correlationId + ": workflow continue: " + response.getResponse().getAnswer());
    }

}
