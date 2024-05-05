package currex;

import currex.lexer.Lexer;
import currex.source.Source;

import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
        try {
            String filename = args[0];
            Source reader = new Source(new FileReader(filename));
            Lexer lexer = new Lexer(reader);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
