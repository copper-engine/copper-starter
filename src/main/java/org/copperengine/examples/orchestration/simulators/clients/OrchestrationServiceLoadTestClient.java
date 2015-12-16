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
package org.copperengine.examples.orchestration.simulators.clients;

import java.net.URL;
import java.util.Random;

import javax.xml.namespace.QName;

import org.copperengine.orchestration.OrchestrationService;
import org.copperengine.orchestration.OrchestrationService_Service;

public final class OrchestrationServiceLoadTestClient {
    private static final QName SERVICE_NAME = new QName("http://orchestration.copperengine.org/", "OrchestrationService");

    private static final Random rnd = new Random();

    private OrchestrationServiceLoadTestClient() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = new URL(args[0]);

        OrchestrationService_Service ss = new OrchestrationService_Service(wsdlURL, SERVICE_NAME);
        OrchestrationService port = ss.getOrchestrationServicePort();

        while(true) {
            StringBuilder sbMsisdn = new StringBuilder("4917");
            for(int i=0; i<8; i++) {
                sbMsisdn.append(rnd.nextInt(10));
            }
            String secret = (rnd.nextInt(10) == 0) ? "sp00n" : "sc00p";            

            port.resetMailbox(sbMsisdn.toString(), secret);
            
            long millis = 50 + rnd.nextInt(500);
            Thread.sleep(millis);
        }        
    }
}
