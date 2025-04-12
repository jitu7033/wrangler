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

package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenType;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

public class TestByteSizeAndTimeDuration {

    private Token parseToken(String input) {
        CharStream cs = CharStreams.fromString(input);
        DirectivesLexer lexer = new DirectivesLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectivesParser parser = new DirectivesParser(tokens);
        DirectivesParser.ValueContext ctx = parser.value(); // Adjust if you use a different rule

        DirectivesVisitorImpl visitor = new DirectivesVisitorImpl();
        return visitor.visit(ctx).getAll().getFirst(); // Assuming TokenGroup is returned
    }

    @Test
    public void testByteSizeParsing() throws Exception {
        Token token = parseToken("10MB");
        Assert.assertNotNull(token);
        Assert.assertTrue(token instanceof ByteSize);

        ByteSize bs = (ByteSize) token;
        Assert.assertEquals(10 * 1024 * 1024L, bs.getBytes()); // Assuming getBytes() returns long
        Assert.assertEquals(TokenType.BYTE_SIZE, bs.type());
    }

    @Test
    public void testTimeDurationParsing() throws Exception {
        Token token = parseToken("500ms");
        Assert.assertNotNull(token);
        Assert.assertTrue(token instanceof TimeDuration);

        TimeDuration td = (TimeDuration) token;
        Assert.assertEquals(500_000_000L, td.getNanoseconds()); // Assuming getNanos() returns nanoseconds
        Assert.assertEquals(TokenType.TIME_DURATION, td.type());
    }
}
