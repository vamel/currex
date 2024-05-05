package currex.source;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SourceTest {

    @Test
    public void readFilePositionTest() throws IOException {
        Reader reader = new FileReader("resources/source/source_test.txt");
        Source source = new Source(reader);
        Assert.assertEquals(source.getPosition().getRow(), 1);
        Assert.assertEquals(source.getPosition().getColumn(), 0);
        Assert.assertFalse(source.isStreamEnd());
    }

    @Test
    public void peekFromFileTest() throws IOException {
        Reader reader = new FileReader("resources/source/source_test.txt");
        Source source = new Source(reader);
        Assert.assertEquals(source.Peek().charValue(), 'L');
        Assert.assertEquals(source.getPosition().getRow(), 1);
        Assert.assertEquals(source.getPosition().getColumn(), 0);
    }

    @Test
    public void readEOFTest() throws IOException {
        Reader reader = new FileReader("resources/source/EOF_test.txt");
        Source source = new Source(reader);
        Assert.assertFalse(source.isStreamEnd());
        source.readChar();
        source.readChar();
        source.readChar();
        Assert.assertTrue(source.isStreamEnd());
    }

    @Test
    public void readCharacterTest() throws IOException {
        Reader reader = new FileReader("resources/source/source_test.txt");
        Source source = new Source(reader);
        Assert.assertNull(source.getCurrentChar());
        source.readChar();
        Assert.assertEquals(source.getCurrentChar().charValue(), 'L');
        Assert.assertEquals(source.Peek().charValue(), 'o');
    }

    @Test
    public void readLineTest() throws IOException {
        Reader reader = new FileReader("resources/source/source_test.txt");
        Source source = new Source(reader);
        source.readChar();
        Assert.assertEquals(source.getCurrentChar().charValue(), 'L');
        Assert.assertEquals(source.Peek().charValue(), 'o');
    }

    @Test
    public void readStringTest() throws IOException {
        Reader reader = new StringReader("Lorem ipsum");
        Source source = new Source(reader);
        source.readChar();
        Assert.assertEquals(source.getCurrentChar().charValue(), 'L');
        Assert.assertEquals(source.Peek().charValue(), 'o');
    }

    @Test
    public void readMultiLineTestOnWindows() throws IOException {
        Reader reader = new FileReader("resources/source/multi_line_test.txt");
        Source source = new Source(reader);
        source.readChar();
        if (System.getProperty("os.name").startsWith("Win"))
        {
            Assert.assertEquals(source.getCurrentChar().charValue(), 'l');
            Assert.assertEquals(source.Peek().charValue(), '\r');
            source.readChar();
            Assert.assertEquals(source.getCurrentChar().charValue(), '\r');
            Assert.assertEquals(source.Peek().charValue(), '\n');
            source.readChar();
            Assert.assertEquals(source.getCurrentChar().charValue(), '\n');
            Assert.assertEquals(source.Peek().charValue(), 'i');
        }
        else if (System.getProperty("os.name").startsWith("Lin")) {
            Assert.assertEquals(source.getCurrentChar().charValue(), 'l');
            Assert.assertEquals(source.Peek().charValue(), '\n');
            source.readChar();
            Assert.assertEquals(source.getCurrentChar().charValue(), '\n');
            Assert.assertEquals(source.Peek().charValue(), 'i');
            source.readChar();
            Assert.assertEquals(source.getCurrentChar().charValue(), 'i');
            Assert.assertEquals(source.Peek().charValue(), '\n');
        }
    }

    @Test
    public void getPositionTest() throws IOException {
        Reader reader = new StringReader("Lorem ipsum");
        Source source = new Source(reader);
        source.readChar();
        Assert.assertEquals(source.getPosition().getRow(), 1);
        Assert.assertEquals(source.getPosition().getColumn(), 1);
    }

    @Test
    public void readEscapedString() throws IOException {
        Reader reader = new StringReader("\\g");
        Source source = new Source(reader);
        source.readChar();
        Assert.assertEquals(source.getCurrentChar().toString(), "\\");
        source.readChar();
        Assert.assertEquals(source.getCurrentChar().charValue(), 'g');
    }

    @Test
    public void testPositionNewLine() throws IOException {
        Reader reader = new FileReader("resources/source/multi_line_test.txt");
        Source source = new Source(reader);
        Assert.assertEquals(source.getPosition().getRow(), 1);
        if (System.getProperty("os.name").startsWith("Win"))
        {
            source.readChar();
            Assert.assertEquals(source.getPosition().getRow(), 1);
            source.readChar();
            Assert.assertEquals(source.getPosition().getRow(), 1);
            source.readChar();
            Assert.assertEquals(source.getPosition().getRow(), 2);
        }
        else if (System.getProperty("os.name").startsWith("Lin")) {
            source.readChar();
            Assert.assertEquals(source.getPosition().getRow(), 1);
            source.readChar();
            Assert.assertEquals(source.getPosition().getRow(), 2);
        }
    }

    @Test
    public void EOFPeekTest() throws IOException {
        Reader reader = new FileReader("resources/source/EOF_test.txt");
        Source source = new Source(reader);
        source.readChar();
        Assert.assertEquals(source.getCurrentChar().charValue(), 'E');
        source.readChar();
        Assert.assertEquals(source.getCurrentChar().charValue(), 'O');
        source.readChar();
        Assert.assertEquals(source.getCurrentChar().charValue(), 'F');
        Assert.assertEquals(source.Peek().charValue(), (char)(-1));
    }
}
