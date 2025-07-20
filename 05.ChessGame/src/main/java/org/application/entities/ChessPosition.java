package org.application.entities;

public class ChessPosition {
    private final int row;
    private final char column;

    public ChessPosition(char column, int row) {

        if (row < 1 || row > 8 || column < 'a' || column > 'h') {
            throw new IllegalArgumentException("Error: Invalid position! Valid positions are from a1 to h8.");
        }

        this.row = row;
        this.column = column;
    }

    public Position toPosition() {
        return new Position(row - 1, this.column - 'a');
    }

    public static ChessPosition toChessPosition(Position position) {
        return new ChessPosition((char)('a' + position.getColumn()), position.getRow() + 1);
    }

    @Override
    public String toString() {
        return column + "" + row;
    }
}
