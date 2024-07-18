package currex.interpreter;

import currex.interpreter.builtin.ConversionTable;
import currex.lexer.Lexer;
import currex.parser.TableParser;
import currex.source.Source;
import currex.structure.components.Program;
import currex.structure.table.TableStatement;

import java.io.FileReader;
import java.io.PrintStream;

public class InterpreterWrapper {
    Source source = new Source(new FileReader("resources/interpreter/currency_table.txt"));
    Lexer lexer = new Lexer(source);
    TableParser tableParser = new TableParser(lexer);
    TableStatement table = tableParser.parse();
    ConversionTable conversionTable = new ConversionTable(table);
    PrintStream printStream = new PrintStream(System.out);
    Interpreter interpreter;

    public InterpreterWrapper() throws Exception {
        interpreter = new Interpreter(conversionTable, printStream);
    }

    public void run(Program program) throws Exception {
        interpreter.run(program);
    }
}
