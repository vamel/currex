package currex.sourcereader;

import currex.token.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class SourceReader {
    private final Reader reader;
    private Position position;
    private boolean isEOF;
    private Character currentChar = null;
    private Character nextChar;

    public SourceReader(Reader reader) throws IOException {
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

    public void skipWhitespaces() {
        try {
            while (Character.isWhitespace(nextChar)) {
                nextChar = readNextChar();
                if (nextChar == '\n') {
                    position.moveToNextRow();
                }
                else {
                    position.moveToNextColumn();
                }
            }
        } catch (IOException ignored) {
        }
    }

    public Character readNextChar() throws IOException {
        int newChar = reader.read();
        if (newChar == -1) {
            isEOF = true;
            return null;
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

    public char readCharX() {
        skipWhitespaces();
        try {
            this.currentChar = this.nextChar;
            this.nextChar = readNextChar();
            if (this.currentChar == '\n') {
                this.position.moveToNextRow();
            } else {
                this.position.moveToNextColumn();
            }
            return this.currentChar;
        } catch (IOException e) {
            return 'z';
        }
    }

    public boolean isStreamEnd() {
        return isEOF && nextChar == null;
    }

    public Position getPosition() {
        return position;
    }
}
