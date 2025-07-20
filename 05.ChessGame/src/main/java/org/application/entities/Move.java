package org.application.entities;

public class Move {
    private final Position from;
    private final Position to;
    private final Color color;

    public Move(Position from, Position to, Color color) {
        this.from = from;
        this.to = to;
        this.color = color;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Color getColor() {
        return color;
    }

}
