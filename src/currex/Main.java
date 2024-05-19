package currex;

import currex.lexer.Lexer;
import currex.parser.Parser;
import currex.source.Source;
import currex.structure.components.Program;

import java.io.FileReader;
import java.io.Reader;

public class Main {

    public static void main(String[] args) {
        try {
            String filename = args[0];
            Reader reader = new FileReader(filename);
            Source source = new Source(reader);
            Lexer lexer = new Lexer(source);
            Parser parser = new Parser(lexer);
            Program program = parser.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
