package currex.token;

public class Position {
    private int row;
    private int column;

    public Position(int column, int row) {
        this.row = row;
        this.column = column;
    }

    public Position(Position position) {
        this.row = position.row;
        this.column = position.column;
    }

    public Position() {
        this.row = 1;
        this.column = 0;
    }

    public void moveToNextRow() {
        row++;
        column = 0;
    }

    public void moveToNextColumn() {
        column++;
    }

    @Override
    public String toString() {
        return "row: " + row + ", column: " + column;
    }
}
