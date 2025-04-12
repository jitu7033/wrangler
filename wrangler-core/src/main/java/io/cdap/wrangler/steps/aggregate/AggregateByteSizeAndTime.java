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


package io.cdap.wrangler.steps.aggregate;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.Row;

import io.cdap.wrangler.api.Aggregator;
import io.cdap.wrangler.api.parser.*;
//import io.cdap.wrangler.api.row.Row;

import java.util.ArrayList;
import java.util.List;

public class AggregateByteSizeAndTime implements Directive, Aggregator {

    private String byteColumn;
    private String timeColumn;
    private String outputByteColumn;
    private String outputTimeColumn;
    private String byteUnit = "B";
    private String  timeUnit = "ms";
    private String aggregationType = "total";

    private long totalBytes = 0;
    private long totalTimeNanos = 0;
    private int rowCount = 0;

    @Override
    public UsageDefinition define() {
        UsageDefinition.Builder builder = UsageDefinition.builder("aggregate-bytes-time");
        builder.define("byteColumn", TokenType.COLUMN_NAME);
        builder.define("timeColumn", TokenType.COLUMN_NAME);
        builder.define("outputByteColumn", TokenType.COLUMN_NAME);
        builder.define("outputTimeColumn", TokenType.COLUMN_NAME);
        builder.define("byteUnit", TokenType.TEXT, true);
        builder.define("timeUnit", TokenType.TEXT, true);
        builder.define("aggregationType", TokenType.TEXT, true);
        return builder.build();
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {
        byteColumn = arguments.value("byteColumn");
        timeColumn = arguments.value("timeColumn");
        outputByteColumn = arguments.value("outputByteColumn");
        outputTimeColumn = arguments.value("outputTimeColumn");

        if (arguments.has("byteUnit")) {
            byteUnit = arguments.value("byteUnit").toString();
        }

        if (arguments.has("timeUnit")) {
            timeUnit = arguments.value("timeUnit").toString();
        }

        if (arguments.has("aggregationType")) {
            aggregationType = arguments.value("aggregationType").toString();
        }
    }
//
//    @Override
//    public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {
//        return List.of();
//    }

    @Override
    public void destroy() {

    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        for (Row row : rows) {
            ByteSize byteSize = (ByteSize) row.getValue(byteColumn);
            TimeDuration duration = (TimeDuration) row.getValue(timeColumn);

            totalBytes += byteSize.getBytes();
            totalTimeNanos += duration.getNanoseconds();
            rowCount++;
        }
        return rows; // Actual aggregation result is in finalize()
    }

    @Override
    public List<Row> finalize(ExecutorContext context)  {
        List<Row> result = new ArrayList<>();

        long finalBytes = aggregationType.equalsIgnoreCase("average")
                ? totalBytes / rowCount : totalBytes;

        long finalTimeNanos = aggregationType.equalsIgnoreCase("average")
                ? totalTimeNanos / rowCount : totalTimeNanos;

        Row row = new Row();

        row.add(outputByteColumn, convertBytes(finalBytes, byteUnit));
        row.add(outputTimeColumn, convertTime(finalTimeNanos, timeUnit));

        result.add(row);
        return result;
    }

    private double convertBytes(long bytes, String unit) {
        switch (unit.toUpperCase()) {
            case "KB": return bytes / 1024.0;
            case "MB": return bytes / (1024.0 * 1024);
            case "GB": return bytes / (1024.0 * 1024 * 1024);
            default: return bytes;
        }
    }

    private double convertTime(long nanos, String unit) {
        switch (unit.toLowerCase()) {
            case "ms": return nanos / 1_000_000.0;
            case "s":
            case "sec": return nanos / 1_000_000_000.0;
            case "min": return nanos / (1_000_000_000.0 * 60);
            default: return nanos;
        }
    }

    @Override
    public List<Row> aggregate(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        return List.of();
    }
}
