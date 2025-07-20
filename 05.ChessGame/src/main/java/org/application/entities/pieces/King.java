package org.application.entities.pieces;

import java.util.List;

import org.application.entities.ChessBoard;
import org.application.entities.Color;
import org.application.entities.Direction;
import org.application.entities.Position;
import org.application.entities.rules.ChessRule;

public class King extends ChessPiece {
    private boolean isFirstMove;

    public King(Color color, Position position) {
        super(color, position);
        this.isFirstMove = true;
    }

    @Override
    public List<Position> pathToPosition(ChessBoard board, Position target) {
        return ChessRule.getPathTo(
            this, target, board, 1,
            Direction.NORTHEAST,
            Direction.NORTHWEST, 
            Direction.SOUTHEAST, 
            Direction.SOUTHWEST,
            Direction.NORTH,
            Direction.SOUTH, 
            Direction.EAST, 
            Direction.WEST
        );
    }

    @Override
    public List<Position> possibleMoves(ChessBoard board) {
        return ChessRule.calculatePossibleMoves(
            this, board, 1,
            Direction.NORTHEAST,
            Direction.NORTHWEST, 
            Direction.SOUTHEAST, 
            Direction.SOUTHWEST,
            Direction.NORTH,
            Direction.SOUTH, 
            Direction.EAST, 
            Direction.WEST
        );
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        this.isFirstMove = false;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }
}
