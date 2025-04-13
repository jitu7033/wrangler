/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * ...
 */

/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Locale;

public class TimeDuration implements Token{

    private final long milliseconds;

    public TimeDuration(String input){
        this.milliseconds = parse(input);
    }

    /**
     * Constructor that parses the input time string and stores its value in milliseconds.
     *
     * @param input Time duration string with units like ms, s, m, h.
     */
    public TimeDuration(String input) {
        this.milliseconds = parse(input);
    }
    /**
     * Parses the input string to extract time duration in milliseconds.
     * Supported units (case-insensitive): ms (milliseconds), s (seconds), m (minutes), h (hours).
     * If no unit is specified, assumes milliseconds.
     *
     * @param input The time string to parse.
     * @return Equivalent duration in milliseconds.
     * @throws NumberFormatException if the numeric part of the string is invalid.
     */

    private long parse(String input){
        input = input.trim().toLowerCase(Locale.ENGLISH);
        if (input.endsWith("ms")) {
            return Long.parseLong(input.replace("ms", ""));
        } else if (input.endsWith("s")) {
            return (long) (Double.parseDouble(input.replace("s", "")) * 1000);
        } else if (input.endsWith("m")) {
            return (long) (Double.parseDouble(input.replace("m", "")) * 60 * 1000);
        } else if (input.endsWith("h")) {
            return (long) (Double.parseDouble(input.replace("h", "")) * 60 * 60 * 1000);
        } else {
            // Assume it's milliseconds if no unit
            return Long.parseLong(input);
        }
    }

    /**
     * @return Time duration in milliseconds.
     */
    public long getMilliseconds() {
        return milliseconds;
    }

    /**
     * @return Time duration in seconds as a double.
     */
    public double getSeconds() {
        return milliseconds / 1000.0;
    }

    /**
     * @return Time duration in minutes as a double.
     */
    public double getMinutes() {
        return milliseconds / (1000.0 * 60);
    }

    /**
     * @return Time duration in hours as a double.
     */

    public double getHours() {
        return milliseconds / (1000.0 * 60 * 60);
    }
    @Override

    /**
     * @return Underlying value for this token (in milliseconds).
     */
    public Object value() {
        return getMilliseconds();
    }

    /**
     * @return Type of token, which is TIME_DURATION.
     */
    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
    }


    /**
     * Converts this token to a JSON element representing milliseconds.
     *
     * @return JSON representation of time in milliseconds.
     */
    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getMilliseconds());
    }



    /**
     * @return Nanoseconds equivalent (not implemented, returns 0).
     */
    @Override
    public long getNanoseconds() {
        return 0;
    }

}

