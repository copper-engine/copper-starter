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

import java.io.Serializable;

public class HelloWorldRequest implements Serializable {

    private static final long serialVersionUID = -190879447387202081L;

    private final String senderName;

    public HelloWorldRequest(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((senderName == null) ? 0 : senderName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HelloWorldRequest other = (HelloWorldRequest) obj;
        if (senderName == null) {
            if (other.senderName != null)
                return false;
        } else if (!senderName.equals(other.senderName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "HelloWorldData [name=" + senderName + "]";
    }

}
