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

import java.io.Serializable;

public class HelloWorldResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String answer;

    public HelloWorldResponse(boolean success, String answer) {
        this.success = success;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "HelloWorldResponse [success=" + success + ", answer="
                + answer + "]";
    }

}
