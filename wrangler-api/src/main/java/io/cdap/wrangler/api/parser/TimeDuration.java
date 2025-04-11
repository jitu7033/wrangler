/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * ...
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

    public long getMilliseconds() {
        return milliseconds;
    }

    public double getSeconds() {
        return milliseconds / 1000.0;
    }

    public double getMinutes() {
        return milliseconds / (1000.0 * 60);
    }

    public double getHours() {
        return milliseconds / (1000.0 * 60 * 60);
    }
    @Override
    public Object value() {
        return getMilliseconds();
    }

    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getMilliseconds());
    }

}

