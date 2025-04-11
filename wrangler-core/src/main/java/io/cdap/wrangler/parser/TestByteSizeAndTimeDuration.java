package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenType;
import org.antlr.v4.runtime.*;
import org.junit.Assert;
import org.junit.Test;

public class TestByteSizeAndTimeDuration {

    private Token parseToken(String input) {
        CharStream cs = CharStreams.fromString(input);
        DirectivesLexer lexer = new DirectivesLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectivesParser parser = new DirectivesParser(tokens);
        DirectivesParser.ValueContext ctx = parser.value();  // adjust if you created custom rule

        DirectivesVisitorImpl visitor = new DirectivesVisitorImpl();  // your custom visitor
        return visitor.visit(ctx).getAll().get(0);  // assuming TokenGroup has getAll()
    }

    @Test
    public void testByteSizeParsing() throws Exception {
        Token token = parseToken("10MB");
        Assert.assertTrue(token instanceof ByteSize);
        ByteSize bs = (ByteSize) token;
        Assert.assertEquals(10 * 1024 * 1024, bs.getBytes());
        Assert.assertEquals(TokenType.BYTE_SIZE, bs.type());
    }

    @Test
    public void testTimeDurationParsing() throws Exception {
        Token token = parseToken("500ms");
        Assert.assertTrue(token instanceof TimeDuration);
        TimeDuration td = (TimeDuration) token;
        Assert.assertEquals(500, td.getMilliseconds());
        Assert.assertEquals(TokenType.TIME_DURATION, td.type());
    }

}
