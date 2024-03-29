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
package org.copperengine.examples.orchestration.wf;

import org.copperengine.core.AutoWire;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Response;
import org.copperengine.core.WaitMode;
import org.copperengine.core.WorkflowDescription;
import org.copperengine.core.audit.AuditTrail;
import org.copperengine.core.persistent.PersistentWorkflow;
import org.copperengine.customerservice.CustomerService;
import org.copperengine.customerservice.GetCustomersByMsisdnRequest;
import org.copperengine.customerservice.GetCustomersByMsisdnResponse;
import org.copperengine.examples.orchestration.adapter.NetworkServiceAdapter;
import org.copperengine.examples.orchestration.adapter.ResetMailboxResponse;
import org.copperengine.examples.orchestration.data.ResetMailboxData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@WorkflowDescription(alias = ResetMailboxDef.NAME, majorVersion = 1, minorVersion = 0, patchLevelVersion = 0)
public class ResetMailbox extends PersistentWorkflow<ResetMailboxData> {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ResetMailbox.class);

    private transient CustomerService customerService;
    private transient NetworkServiceAdapter networkServiceAdapter;
    private transient AuditTrail auditTrail;
    public java.util.Map<String,Object> mapTest = new HashMap<>();
    public java.util.List<String> listTest = new ArrayList<>();
    public String[] arrayTest = new String[2];
    public java.util.Date dateTest = null;
    public java.sql.Date sqlDateTest = null;
    public java.time.ZonedDateTime zonedDateTimeTest = null;
    public java.time.Instant instantTest = null;


    @AutoWire
    public void setAuditTrail(AuditTrail auditTrail) {
        this.auditTrail = auditTrail;
    }

    @AutoWire
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @AutoWire
    public void setNetworkServiceAdapter(NetworkServiceAdapter networkServiceAdapter) {
        this.networkServiceAdapter = networkServiceAdapter;
    }

    @Override
    public void main() throws Interrupt {
        logger.info("workflow instance started");
        auditTrail.synchLog(1, new Date(), "conversationId", "context", this.getId(), null, null, "workflow instance started", "");

        this.dateTest = new java.util.Date();
        this.sqlDateTest = new java.sql.Date(this.dateTest.getTime());
        this.zonedDateTimeTest = ZonedDateTime.now();
        this.instantTest = this.dateTest.toInstant();


        this.arrayTest[0] = "testString1";
        this.arrayTest[1] = "testString2";

        this.listTest.add("List String 1");
        this.listTest.add("List String 2");

        this.mapTest.put("Key1", "Value1");
        this.mapTest.put("Key2", "Value2");
        this.mapTest.put("Key_Date", this.dateTest);
        this.mapTest.put("Key_Array", this.arrayTest);
        this.mapTest.put("Key_List", this.listTest);




        try {
            //Simulate RUNNING state of workflow for 30 seconds
            Thread.sleep(30_000);
        } catch (java.lang.InterruptedException ex) {}


        //Simulate WAITING state of workflow for 3 minutes
        sleep(180);

        if (!checkSecretOK()) {
            sendSms("Authentication failed");
        } else {
            if (resetMailbox()) {
                sendSms("Mailbox reset successfully executed");
            } else {
                sendSms("Unable to reset mailbox - please try again later");
            }
        }
        logger.info("workflow instance finished");
    }

    private boolean checkSecretOK() throws Interrupt {
        for (int i = 0; ; i++) {
            try {
                GetCustomersByMsisdnRequest parameters = new GetCustomersByMsisdnRequest();
                parameters.setMsisdn(getData().msisdn());
                GetCustomersByMsisdnResponse response = customerService.getCustomersByMsisdn(parameters);
                logger.debug("Received customer data: {}", response.getReturn());
                return getData().secret().equals(response.getReturn().getSecret());
            } catch (Exception e) {
                logger.error("checkSecretOK failed", e);
            }
            if (i < 2) {
                sleep(5);
            } else {
                break;
            }
        }
        return false;
    }

    private boolean resetMailbox() throws Interrupt {
        for (int i = 0; ; i++) {
            final String correlationId = networkServiceAdapter.resetMailbox(getData().msisdn());
            wait(WaitMode.ALL, 5 * 60 * 60 * 1000, correlationId);
            final Response<ResetMailboxResponse> response = getAndRemoveResponse(correlationId);
            if (response.isTimeout()) {
                logger.warn("resetMailbox request timed out");
            } else if (response.getException() != null) {
                logger.error("resetMailbox request failed", response.getException());
            } else if (!response.getResponse().isSuccess()) {
                logger.info("resetMailbox request failed - success = false in response");
            } else {
                logger.info("resetMailbox succeeded");
                return true;
            }
            if (i == 5) {
                logger.error("reset mailbox failed - max number of retries reached");
                return false;
            }
            sleep(5);
        }

    }

    private void sendSms(String msg) throws Interrupt {
        logger.info("sendSMS({})", msg);
        networkServiceAdapter.sendSMS(getData().msisdn(), msg);
    }

    private void sleep(int seconds) throws Interrupt {
        logger.info("Sleeping {} seconds up to next try...", seconds);
        wait(WaitMode.ALL, seconds * 1000, getEngine().createUUID());
    }

}
