package currex.interpreter;

import currex.interpreter.error.*;
import currex.lexer.Lexer;
import currex.parser.Parser;
import currex.source.Source;
import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Program;
import currex.structure.primitives.PrimitiveType;
import currex.structure.primitives.StringPrimitive;
import currex.structure.statements.DeclarationStatement;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class InterpreterTest {

    @Test
    public void CreateInterpreter() throws Exception {
        Program program = new Program(Map.of(
                "main",
                new FunctionDefinition(
                        PrimitiveType.NONE,
                        "main",
                        List.of(),
                        new Block(List.of(
                                new DeclarationStatement(PrimitiveType.STRING, "value",
                                        new StringPrimitive("text"))
                        ))
                )));
        InterpreterWrapper interpreter = new InterpreterWrapper();
        interpreter.run(program);
    }

    @Test
    public void MainFuncitonNotFoundError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/main_not_found.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        MainFunctionNotDefinedError e = Assert.assertThrows(MainFunctionNotDefinedError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidVariableTypeError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_variable_type.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidVariableTypeError e = Assert.assertThrows(InvalidVariableTypeError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void VariableAlreadyDeclaredError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/var_already_declared.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        VariableAlreadyExistsError e = Assert.assertThrows(VariableAlreadyExistsError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void VariableDoesNotExistError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/var_not_exist.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        VariableDoesNotExistError e = Assert.assertThrows(VariableDoesNotExistError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidFunctionCall() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_func_call.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidFunctionCallError e = Assert.assertThrows(InvalidFunctionCallError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidFunctionReturnType() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_return.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidReturnValueError e = Assert.assertThrows(InvalidReturnValueError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void ZeroDivisionError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/zero_division.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        ZeroDivisionError e = Assert.assertThrows(ZeroDivisionError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidIfBoolValueError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_bool.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidBoolValueError e = Assert.assertThrows(InvalidBoolValueError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidWhileBoolValueError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_bool2.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidBoolValueError e = Assert.assertThrows(InvalidBoolValueError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidCurrencyComparisonError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/currency_comparison.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidCurrencyNameError e = Assert.assertThrows(InvalidCurrencyNameError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidMethodCallError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_method_call.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidVariableTypeError e = Assert.assertThrows(InvalidVariableTypeError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void MethodDoesNotExistError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/method_not_exist.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        FunctionDoesNotExistError e = Assert.assertThrows(FunctionDoesNotExistError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidAccessError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_access.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidMethodCallError e = Assert.assertThrows(InvalidMethodCallError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidNegationMinusError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_negation_minus.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        IncompatibleTypesError e = Assert.assertThrows(IncompatibleTypesError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidNegationExclamationError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_negation_exclamation.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        IncompatibleTypesError e = Assert.assertThrows(IncompatibleTypesError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidCurrencyCastError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_currency_cast.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidCurrencyNameError e = Assert.assertThrows(InvalidCurrencyNameError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void InvalidCurrencyConversionError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/invalid_currency_conversion.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        InvalidCurrencyNameError e = Assert.assertThrows(InvalidCurrencyNameError.class,
                () -> interpreter.run(program));
    }

    @Test
    public void CurrencyNotDefinedError() throws Exception {
        Source source = new Source(new FileReader("resources/interpreter/currency_not_defined.txt"));
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        InterpreterWrapper interpreter = new InterpreterWrapper();
        VariableDoesNotExistError e = Assert.assertThrows(VariableDoesNotExistError.class,
                () -> interpreter.run(program));
    }
}
