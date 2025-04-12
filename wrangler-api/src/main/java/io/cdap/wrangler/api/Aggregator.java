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




package io.cdap.wrangler.api;

import java.util.List;

/**
 * Aggregator interface for directives that perform aggregation over rows.
 */
public interface Aggregator extends Directive {
    /**
     * Called once per group of rows to calculate and return the aggregated result.
     *
     * @param rows the list of rows in the group
     * @param context context passed during execution
     * @return a list of aggregated rows (usually 1 row)
     */
    List<Row> aggregate(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException;
}
