package currex.source;

import currex.token.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class Source {
    private final Reader reader;
    private final Position position;
    private boolean isEOF;
    private Character currentChar = null;
    private Character nextChar;

    public Source(Reader reader) throws IOException {
        this.position = new Position();
        this.reader = new BufferedReader(reader);
        this.nextChar = this.readNextChar();
        this.isEOF = false;
    }

    public Character getCurrentChar() {
        return currentChar;
    }

    public Character Peek() {
        return nextChar;
    }

    private char readNextChar() throws IOException {
        int newChar = reader.read();
        if (newChar == -1) {
            isEOF = true;
        }
        return (char) newChar;
    }

    public void readChar() {
        try {
            currentChar = nextChar;
            nextChar = readNextChar();
            if (currentChar == '\n') {
                position.moveToNextRow();
            } else {
                position.moveToNextColumn();
            }
        } catch (IOException ignored) {
        }
    }

    public boolean isStreamEnd() {
        return isEOF;
    }

    public Position getPosition() {
        return position;
    }
}
