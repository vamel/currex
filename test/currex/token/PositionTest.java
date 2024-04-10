package currex.token;

import org.junit.Assert;
import org.junit.Test;

public class PositionTest {

    @Test
    public void testGetRow() {
        Position position = new Position();
        Assert.assertEquals(position.getRow(), 1);
    }

    @Test
    public void testGetColumn() {
        Position position = new Position();
        Assert.assertEquals(position.getColumn(), 0);
    }

    @Test
    public void testMoveToNextRow() {
        Position position = new Position();
        position.moveToNextRow();
        Assert.assertEquals(position.getRow(), 2);
    }

    @Test
    public void testMoveToNextColumn() {
        Position position = new Position();
        position.moveToNextColumn();
        Assert.assertEquals(position.getColumn(), 1);
    }

    @Test
    public void resetColumnAfterMoving() {
        Position position = new Position();
        position.moveToNextColumn();
        position.moveToNextColumn();
        position.moveToNextColumn();
        position.moveToNextColumn();
        position.moveToNextColumn();
        position.moveToNextRow();
        Assert.assertEquals(position.getColumn(), 0);
    }

    @Test
    public void testConstructor() {
        Position position = new Position();
        Assert.assertEquals(position.getColumn(), 0);
        Assert.assertEquals(position.getRow(), 1);
    }

    @Test
    public void testCopyingConstructor() {
        Position position = new Position();
        position.moveToNextRow();
        position.moveToNextColumn();
        Position copy = new Position(position);
        Assert.assertEquals(copy.getRow(), 2);
        Assert.assertEquals(copy.getColumn(), 1);
    }

    @Test
    public void testToString() {
        Position position = new Position();
        position.moveToNextRow();
        position.moveToNextColumn();
        Assert.assertEquals(position.toString(), "row: 2, column: 1");
    }
}
