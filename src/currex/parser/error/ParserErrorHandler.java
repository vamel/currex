package currex.parser.error;

import currex.token.Position;

public class ParserErrorHandler {
    public ParserErrorHandler() {}

    public void handleParserError(Exception exception, Position position) throws Exception {
        System.out.println("An error has occured in line "
                + position.getRow()
                + " in column "
                + position.getColumn() + ":\n"
                + exception.getMessage());
        throw exception;
    }
}
