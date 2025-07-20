package org.application.entities.pieces;

import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Color;
import org.application.entities.Position;

public abstract class ChessPiece {
    private Color color;
    private Position position;
    private boolean canMove;

    public ChessPiece(Color color, Position position) {
        this.color = color;
        this.position = position;
        this.canMove = true;
    }

    public abstract List<Position> pathToPosition(ChessBoard board, Position target);
    public abstract List<Position> possibleMoves(ChessBoard board);

    public boolean canMoveToPosition(ChessBoard board, Position target) {
        return this.possibleMoves(board).contains(target);
    }

    public boolean isThereAnyPossibleMove(ChessBoard board) {
        return !this.possibleMoves(board).isEmpty();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean getCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
