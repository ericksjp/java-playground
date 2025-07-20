package org.application.entities;

public enum Direction {
    NORTH(1, 0),
    SOUTH(-1, 0),
    EAST(0, 1),
    WEST(0, -1),
    NORTHEAST(1, 1),
    NORTHWEST(1, -1),
    SOUTHEAST(-1, 1),
    SOUTHWEST(-1, -1);

    private final int rowOffset;
    private final int columnOffset;

    Direction(int rowOffset, int columnOffset) {
        this.rowOffset = rowOffset;
        this.columnOffset = columnOffset;
    }

    public int getRowOffset() {
        return rowOffset;
    }

    public int getColumnOffset() {
        return columnOffset;
    }
}
