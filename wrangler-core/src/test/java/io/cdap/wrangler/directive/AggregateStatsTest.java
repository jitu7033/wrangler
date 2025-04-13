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

package io.cdap.wrangler.directive;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveContext;
import io.cdap.wrangler.api.DirectiveExecutionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsTest {

    @Test
    public void testAggregateStats_basic() throws Exception {
        List<Row> inputRows = Arrays.asList(
                new Row("size", "1KB").add("duration", "1s"),
                new Row("size", "2KB").add("duration", "2s"),
                new Row("size", "512B").add("duration", "500ms")
        );

        AggregateStats directive = new AggregateStats("size", "duration");
        List<Row> output = directive.execute(inputRows);

        Assert.assertEquals(1, output.size());
        Row result = output.get(0);

        Assert.assertEquals(3584L, result.getValue("total_size_mb"));
        Assert.assertEquals(3500L, result.getValue("total_time_sec"));
    }

    public void testAggregateStats_basic() throws Exception {
        List<Row> inputRows = Arrays.asList(
                new Row("size", "1KB").add("duration", "1s"),
                new Row("size", "2KB").add("duration", "2s"),
                new Row("size", "512B").add("duration", "500ms")
        );

        AggregateStats directive = new AggregateStats("size", "duration");
        List<Row> output = directive.execute(inputRows);

        Assert.assertEquals(1, output.size());
        Row row = output.get(0);

        // Check aggregated fields
        Assert.assertEquals(3584L, row.getValue("total_size_mb")); // 1KB + 2KB + 0.5KB = 3.5KB = 3584B
        Assert.assertEquals(3500L, row.getValue("total_time_sec")); // 1s + 2s + 0.5s = 3.5s = 3500ms
    }

    @Test
    public void testAggregateStats_invalidValues() throws Exception {
        List<Row> inputRows = Arrays.asList(
                new Row("size", "notASize").add("duration", "2s"),
                new Row("size", "2KB").add("duration", "oops")
        );

        AggregateStats directive = new AggregateStats("size", "duration");
        List<Row> output = directive.execute(inputRows);

        Row row = output.get(0);
        Assert.assertEquals(2048L, row.getValue("total_size_mb")); // Only valid size
        Assert.assertEquals(2000L, row.getValue("total_time_sec")); // Only valid duration
    }


    public void testAggregateStatsDirectiveUsingRecipe() throws Exception {
        // Step 1: Input data
        List<Row> inputRows = Arrays.asList(
                new Row("data_transfer_size", "1MB").add("response_time", "1s"),
                new Row("data_transfer_size", "512KB").add("response_time", "500ms"),
                new Row("data_transfer_size", "256KB").add("response_time", "250ms")
        );

        // Step 2: Recipe to run (name should match your directive)
        String[] recipe = new String[] {
                "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        // Step 3: Execute directive using TestingRig
        List<Row> results = TestingRig.execute(recipe, inputRows);

        // Step 4: Expected results
        double expectedTotalSizeInMB = (1.0 + 0.5 + 0.25); // 1MB + 512KB + 256KB
        double expectedTotalTimeInSec = (1.0 + 0.5 + 0.25); // 1s + 500ms + 250ms

        // Step 5: Assertions
        Assert.assertEquals(1, results.size());

        Row result = results.get(0);


        // Use tolerance for double comparison
        Assert.assertEquals(expectedTotalSizeInMB,
                (double) result.getValue("total_size_mb"), 0.0001);

        Assert.assertEquals(expectedTotalTimeInSec,
                (double) result.getValue("total_time_sec"), 0.0001);
    }


}
