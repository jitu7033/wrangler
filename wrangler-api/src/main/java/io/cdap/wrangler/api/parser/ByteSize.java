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

public class ByteSize implements Token {

    private final long bytes;

    /**
     * Constructor that parses a size string (e.g., "10KB", "2MB", "1GB")
     * and stores the equivalent size in bytes.
     *
     * @param input String representing the data size with or without units.
     */
    public ByteSize(String input){
        this.bytes = parse(input);
    }

    /**
     * Parses a string representing a data size (e.g., "10KB", "2.5MB", "1GB") into bytes.
     * Supports units: B, KB, MB, GB (case-insensitive).
     * If no unit is specified, the input is assumed to be in bytes.
     *
     * @param input The size string to parse.
     * @return The equivalent size in bytes as a long value.
     * @throws NumberFormatException if the input is not a valid number.
     */

    private long parse(String input){
        input = input.trim().toUpperCase(Locale.ENGLISH);
        if(input.endsWith("KB")){
            return (long) (Double.parseDouble(input .replace("KB","")) * 1024);
        }
        else if(input.endsWith("MB")){
            return (long) (Double.parseDouble(input.replace("MB","")) * 1024 * 1024);
        }
        else if(input.endsWith("GB")) {
            return (long) (Double.parseDouble(input.replace("GB", "")) * 1024 * 1024 * 1024);
        }
        else if(input.endsWith("B")){
            return Long.parseLong(input.replace("B",""));
        }
        else{
            // Assume it's in byte if no unit
            return Long.parseLong(input);
        }
    }

    /**
     * @return Parsed size in bytes.
     */
    public long getBytes() {
        return bytes;
    }

    /**
     * @return Size in kilobytes (KB).
     */
    public double getKilobytes() {
        return bytes / 1024.0;
    }

    /**
     * @return Size in megabytes (MB).
     */
    public double getMegabytes() {
        return bytes / (1024.0 * 1024);
    }
    /**
     * @return Size in gigabytes (GB).
     */
    public double getGigabytes() {
        return bytes / (1024.0 * 1024 * 1024);
    }


    /**
     * @return The raw value (in bytes) of this token.
     */
    @Override
    public Object value() {
        return getBytes();
    }

    /**
     * @return Token type enum for BYTE_SIZE.
     */
    @Ove
    @Override
    public TokenType type() {
        return TokenType.BYTE_SIZE;
    }

    /**
     * @return A JSON representation of the byte size.
     */
    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getBytes());
    }

    /**
     * Unused for this token type. Always returns 0.
     */
    @Override
    public long getNanoseconds() {
        return 0;
    }

    /**
     * @return String representation of byte size (e.g., "1024bytes").
     */
    @Override
    public String toString(){
        return bytes + "bytes";
    }
}