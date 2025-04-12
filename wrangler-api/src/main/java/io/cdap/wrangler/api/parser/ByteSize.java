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

    public ByteSize(String input){
        this.bytes = parse(input);
    }


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

    public long getBytes() {
        return bytes;
    }

    public double getKilobytes() {
        return bytes / 1024.0;
    }

    public double getMegabytes() {
        return bytes / (1024.0 * 1024);
    }

    public double getGigabytes() {
        return bytes / (1024.0 * 1024 * 1024);
    }



    @Override
    public Object value() {
        return getBytes();
    }

    @Override
    public TokenType type() {
        return TokenType.BYTE_SIZE;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getBytes());
    }

    @Override
    public long getNanoseconds() {
        return 0;
    }

    @Override
    public String toString(){
        return bytes + "bytes";
    }
}