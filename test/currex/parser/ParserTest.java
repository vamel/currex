package currex.parser;

import currex.lexer.Lexer;
import currex.source.Source;
import org.junit.Test;

import java.io.FileReader;

public class ParserTest {
    @Test
    public void createParserTest() {

    }

    @Test
    public void ParseIntegerParameterTest() throws Exception {
        Source reader = new Source(new FileReader("resources/lexer/integer_declaration.txt"));
        Lexer lexer = new Lexer(reader);
        Parser parser = new Parser(lexer);
//        Parameter parameter = parser.Parse();
//        Assert.assertEquals("divide_and_conquer", parameter.name());
//        Assert.assertEquals(PrimitiveType.INTEGER, parameter.type());
    }

    @Test
    public void ParseFloatParameterTest() {

    }

    @Test
    public void ParseStringParameterTest() {

    }

    @Test
    public void ParseBoolParameterTest() {

    }

    @Test
    public void ParseCurrencyParameterTest() {

    }
}
