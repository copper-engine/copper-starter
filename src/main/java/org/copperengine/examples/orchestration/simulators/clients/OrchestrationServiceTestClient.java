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

import javax.xml.namespace.QName;

import org.copperengine.orchestration.OrchestrationService;
import org.copperengine.orchestration.OrchestrationService_Service;

public final class OrchestrationServiceTestClient {

    private static final QName SERVICE_NAME = new QName("http://orchestration.copperengine.org/", "OrchestrationService");

    private OrchestrationServiceTestClient() {
    }

    public static void main(String args[]) throws java.lang.Exception{
        URL wsdlURL = new URL("http://localhost:" +
                ("V2".equals(System.getenv("COP_STARTER_VERSION")) ? "9093" : "9092" ) + "/services/orchestration?wsdl");
        String resetMailbox_msisdn = "491716677889";
        String resetMailbox_secret = "sc00p";
        int times = 2;

        if (args.length >= 3) {
            wsdlURL = new URL(args[0]);
            resetMailbox_msisdn = args[1];
            resetMailbox_secret = args[2];
        }
        if (args.length >= 4 && args[3] != null ) {
            times = Integer.valueOf(args[3]);
        }


        System.out.println("Will simulate " + times + " broken workflows");
        for(int i = 0; i< times; i++) {

            OrchestrationService_Service ss = new OrchestrationService_Service(wsdlURL, SERVICE_NAME);
            OrchestrationService port = ss.getOrchestrationServicePort();

            port.resetMailbox(resetMailbox_msisdn, resetMailbox_secret);
        }

        System.out.println("Done");
    }

}
