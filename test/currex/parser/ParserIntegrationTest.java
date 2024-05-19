package currex.parser;

import currex.lexer.Lexer;
import currex.source.Source;
import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Program;
import currex.structure.expressions.AdditionExpression;
import currex.structure.expressions.Expression;
import currex.structure.expressions.IdentifierExpression;
import currex.structure.primitives.PrimitiveType;
import currex.structure.primitives.StringPrimitive;
import currex.structure.statements.AssignmentStatement;
import currex.structure.statements.Statement;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.Reader;

public class ParserIntegrationTest {

    @Test
    public void parserGetLexerTest() throws Exception {
        Reader reader = new FileReader("resources/parser/simple_void_function.txt");
        Source source = new Source(reader);
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
    }

    @Test
    public void readFunctionAmountTest() throws Exception {
        Reader reader = new FileReader("resources/parser/simple_void_function.txt");
        Source source = new Source(reader);
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Assert.assertEquals(1, program.functionDefinitions().size());
        Assert.assertTrue(program.functionDefinitions().containsKey("print"));
    }

    @Test
    public void readFunctionContentTest() throws Exception {
        Reader reader = new FileReader("resources/parser/simple_void_function.txt");
        Source source = new Source(reader);
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Assert.assertEquals(1, program.functionDefinitions().size());
        FunctionDefinition function = program.functionDefinitions().get("print");
        Assert.assertEquals(1, function.parameters().size());
        Assert.assertEquals("message", function.parameters().get(0).name());
        Assert.assertEquals(PrimitiveType.STRING, function.parameters().get(0).type());
        Block block = function.block();
        Assert.assertEquals(1, block.statementList().size());
        Statement statement = block.statementList().get(0);
        Assert.assertEquals(AssignmentStatement.class, statement.getClass());
        AssignmentStatement assignmentStatement = (AssignmentStatement) statement;
        Expression left = assignmentStatement.left();
        Expression right = assignmentStatement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(AdditionExpression.class, right.getClass());
        IdentifierExpression identifier = (IdentifierExpression) left;
        AdditionExpression addition = (AdditionExpression) right;
        Assert.assertEquals("message", identifier.name());
        Expression additionLeft = addition.left();
        Expression additionRight = addition.right();
        Assert.assertEquals(IdentifierExpression.class, additionLeft.getClass());
        Assert.assertEquals(StringPrimitive.class, additionRight.getClass());
        IdentifierExpression leftIdentifier = (IdentifierExpression) additionLeft;
        StringPrimitive rightPrimitive = (StringPrimitive) additionRight;
        Assert.assertEquals("message", leftIdentifier.name());
        Assert.assertEquals("\".\"", rightPrimitive.value());
    }

    @Test
    public void emptyLexerTest() throws Exception {
        Reader reader = new FileReader("resources/parser/empty_file.txt");
        Source source = new Source(reader);
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Assert.assertEquals(0, program.functionDefinitions().size());
    }
}
