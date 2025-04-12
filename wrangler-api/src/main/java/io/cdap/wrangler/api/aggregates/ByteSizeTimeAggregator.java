/*
 *  Copyright © 2017-2019 Cask Data, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */


package io.cdap.wrangler.api.aggregates;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.Aggregator;
import io.cdap.wrangler.api.parser.*;

//import io.cdap.wrangler.api.annotations.Name;


import java.util.ArrayList;
import java.util.List;

@Name("aggregate-bytesize-time")
@Description("Aggregates total/average byte size and time duration values.")
public class ByteSizeTimeAggregator implements Aggregator {
    private String sizeSourceCol;
    private String timeSourceCol;
    private String sizeTargetCol;
    private String timeTargetCol;

    @Override
    public UsageDefinition define() {
        return UsageDefinition.builder("aggregate-bytesize-time")
                .define("size_col", TokenType.COLUMN_NAME);
    }

    @Override
    public List<Row> finalize(ExecutorContext context) {
        return List.of();
    }

    @Override
    public void initialize(Arguments arguments) throws DirectiveParseException {
        sizeSourceCol = arguments.value("size_col");
        timeSourceCol = arguments.value("time_col");
        sizeTargetCol = arguments.value("total_size_col");
        timeTargetCol = arguments.value("total_time_col");
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException, ErrorRowException, ReportErrorAndProceed {
        return List.of();
    }

    @Override
    public void destroy() {

    }

    @Override
    public List<Row> aggregate(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        long totalBytes = 0;
        long totalTimeNanos = 0;

        for (Row row : rows) {
            Object sizeVal = row.getValue(sizeSourceCol);
            Object timeVal = row.getValue(timeSourceCol);

            if (sizeVal instanceof ByteSize) {
                totalBytes += ((ByteSize) sizeVal).getBytes();
            }

            if (timeVal instanceof TimeDuration) {
                totalTimeNanos += ((TimeDuration) timeVal).getNanoseconds();
            }
        }

        List<Row> result = new ArrayList<>();
        Row aggregated = new Row();
        aggregated.add(sizeTargetCol, totalBytes);
        aggregated.add(timeTargetCol, totalTimeNanos);
        result.add(aggregated);

        return result;
    }
}
