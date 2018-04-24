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
package org.copperengine.examples.orchestration.enginemon;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OrchestrationEngineWithMonitoring {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String context = "orchestrationWithMonitoring/jetty.xml";

        if ("V2".equals(System.getenv("COP_STARTER_VERSION")) || (args.length > 0 && "V2".equals(args[0]))) {
            context = "orchestrationWithMonitoring/jetty_2.xml";
            System.out.println("Starting alternative version of orchestration\n");
        }

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(context);
        ctx.registerShutdownHook();

    }

}
