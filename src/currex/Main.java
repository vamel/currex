package currex;

import currex.interpreter.Interpreter;
import currex.interpreter.builtin.ConversionTable;
import currex.lexer.Lexer;
import currex.parser.Parser;
import currex.parser.TableParser;
import currex.source.Source;
import currex.structure.components.Program;
import currex.structure.table.TableStatement;

import java.io.FileReader;
import java.io.PrintStream;
import java.io.Reader;

public class Main {

    public static void main(String[] args) {
        try {
            String inputFilename = args[0];
            PrintStream printer = new PrintStream(System.out);
            Reader reader = new FileReader(inputFilename);
            Source source = new Source(reader);
            Lexer lexer = new Lexer(source);
            Parser parser = new Parser(lexer);
            Program program = parser.parse();

            String conversionTableFilename = args[1];
            Reader conversionTableReader = new FileReader(conversionTableFilename);
            Source conversionTableSource = new Source(conversionTableReader);
            Lexer tableLexer = new Lexer(conversionTableSource);
            TableParser tableParser = new TableParser(tableLexer);
            TableStatement table = tableParser.parse();
            ConversionTable conversionTable = new ConversionTable(table);

            Interpreter interpreter = new Interpreter(conversionTable, printer);
            interpreter.run(program);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
